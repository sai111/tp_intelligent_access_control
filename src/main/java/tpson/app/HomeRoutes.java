package tpson.app;


import com.jfinal.config.Routes;
import tpson.controller.IndexController;
import tpson.controller.LoginController;
import tpson.controller.people.*;

/**
 * Created by liuandong on 2017/4/20.
 */
public class HomeRoutes extends Routes {
    public void config() {
        add("/", IndexController.class);
        add("/login", LoginController.class);

        add("/population", PopulationController.class);
        add("/visitor", VisitorController.class);
        add("/access", AccessController.class);
        add("/people_alarm", AlarmController.class);
        add("/resident", ResidentController.class);
    }
}
