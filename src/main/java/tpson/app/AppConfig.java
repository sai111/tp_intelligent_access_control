package tpson.app;

import com.jfinal.authz.AuthzInterceptor;
import com.jfinal.authz.AuthzPlugin;
import com.jfinal.config.*;
import com.jfinal.core.Const;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.swagger.config.routes.SwaggerRoutes;
import com.jfinal.template.Engine;
import tpson.intercepter.SessionIntercept;
import tpson.model._MappingKit;

public class AppConfig extends JFinalConfig {

    private Routes routes;

    @Override
    public void afterJFinalStart() {

    }

    @Override
    public void beforeJFinalStop() {

    }

    @Override
    public void configConstant(Constants constants) {
        constants.setDevMode(true);
        constants.setMaxPostSize(100 * Const.DEFAULT_MAX_POST_SIZE);
        constants.setErrorView(401, "/login");
        constants.setErrorView(403, "/login");
        constants.setError404View("/Home/common/404.html");
        constants.setError500View("/Home/common/500.html");
    }

    @Override
    public void configRoute(Routes routes) {
        this.routes = routes;
        routes.add(new HomeRoutes());
        routes.add(new ApiRoutes());
        routes.add(new SwaggerRoutes());
    }

    @Override
    public void configEngine(Engine me) {
    }

    @Override
    public void configPlugin(Plugins plugins) {
        String url = "jdbc:mysql://" + WebConfig.db_ip + "/tp_iac?serverTimezone=GMT%2b8&autoReconnect=true&failOverReadOnly=false&useUnicode=true&characterEncoding=UTF-8";
        DruidPlugin druidPlugin = new DruidPlugin(url, WebConfig.db_user, WebConfig.db_password);
        druidPlugin.setInitialSize(1);
        plugins.add(druidPlugin);

        // redis服务
        RedisPlugin redis = new RedisPlugin("iac", WebConfig.redis_ip, WebConfig.redis_port,5000, WebConfig.redis_password,3);
        plugins.add(redis);

        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
        activeRecordPlugin.setShowSql(WebConfig.is_debug);
        _MappingKit.mapping(activeRecordPlugin);
        plugins.add(activeRecordPlugin);

        if (WebConfig.is_release) {
            AuthzPlugin authzPlugin = new AuthzPlugin(this.routes);
            authzPlugin.setLoginUrl("/login");
            authzPlugin.setAuthzDenyUrl("/Home/common/error.html");
            plugins.add(authzPlugin);
        }

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {
        interceptors.add(new SessionIntercept());
        interceptors.add(new SessionInViewInterceptor());
        interceptors.addGlobalServiceInterceptor(new Tx());

        if (WebConfig.is_release) {
            interceptors.add(new AuthzInterceptor());
        }
    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}
