package tpson.app;

import com.jfinal.core.JFinalFilter;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Created by liuandong on 2017/6/5.
 */
public class App {
    private static final Logger logger = Logger.getLogger(App.class);
    private static Server server;
    public static void main(String[] args) throws Exception {
        WebConfig.init();
        WebConfig.logParam();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                if(server != null)
                    server.stop();
                logger.info("系统正常关闭");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }));

        start();
    }

    private static void start() throws Exception {
        server = new Server(WebConfig.web_listen_port);
        WebAppContext webapp = new WebAppContext("web","/");

        if (WebConfig.is_debug){
            webapp.setDefaultsDescriptor("webdefault.xml");
        }

        FilterHolder fh = webapp.addFilter(JFinalFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        fh.setInitParameter("configClass","tpson.app.AppConfig");
        webapp.addFilter(fh,"/*", EnumSet.of(DispatcherType.REQUEST));
        webapp.setResourceBase("src/main/webapp/");

        server.setHandler(webapp);
        server.start();
        server.join();
    }
}
