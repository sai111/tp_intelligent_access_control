package com.jfinal.swagger.config.routes;

import com.jfinal.config.Routes;
import com.jfinal.swagger.controller.SwaggerController;

/**
 * [description]
 *
 * @author lee
 * @version V1.0.0
 * @date 2017/7/8
 */
public class SwaggerRoutes extends Routes {

    @Override
    public void config() {
        add("/test", SwaggerController.class);
    }

}
