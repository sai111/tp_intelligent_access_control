package com.jfinal.swagger.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jfinal.breadmaker.config.BaseConfig;
import com.jfinal.breadmaker.utils.DbConfig;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.swagger.annotation.*;
import com.jfinal.swagger.model.SwaggerDoc;
import com.jfinal.swagger.model.SwaggerPath;
import com.jfinal.swagger.utils.ClassHelper;
import com.jfinal.swagger.utils.DocUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * swagger
 *
 * @author lee
 * @version V1.0.0
 * @date 2017/7/7
 */
public class SwaggerController extends Controller {

    public void index() {
        render("/Swagger/index.html");
    }
    
    public void api() {
        renderJson(DocUtil.getDoc("tpson.controller"));
    }

    private static String getType(Class c){
        return c.getSimpleName();
    }
}
