package com.jfinal.swagger.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jfinal.breadmaker.config.BaseConfig;
import com.jfinal.breadmaker.utils.DbConfig;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.swagger.annotation.Api;
import com.jfinal.swagger.annotation.ApiOperation;
import com.jfinal.swagger.annotation.Param;
import com.jfinal.swagger.annotation.Params;
import com.jfinal.swagger.model.SwaggerDoc;
import com.jfinal.swagger.model.SwaggerPath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by liuandong on 2017/8/24.
 */
public class DocUtil {
    public static String getDoc(String packageStr){
        SwaggerDoc doc = new SwaggerDoc();
        Map<String, Map<String, SwaggerPath.ApiMethod>> paths = new HashMap<>();
        Map<String, String> classMap = Maps.newHashMap();
        Set<Class<?>> classSet = ClassHelper.getApiSet(packageStr);
        for (Class<?> cls : classSet) {
            if (cls.isAnnotationPresent(Api.class)) {
                Api api = cls.getAnnotation(Api.class);

                if (!classMap.containsKey(api.tag())) {
                    classMap.put(api.tag(), api.description());
                }

                Method[] methods = cls.getMethods();

                for (Method method : methods) {
                    Annotation[] annotations = method.getAnnotations();
                    SwaggerPath.ApiMethod apiMethod = new SwaggerPath.ApiMethod();
                    apiMethod.setOperationId("");
                    apiMethod.addProduce("application/json");
                    boolean hasApi = false;

                    for (Annotation annotation : annotations) {
                        Class<? extends Annotation> annotationType = annotation.annotationType();
                        if (ApiOperation.class == annotationType) {
                            ApiOperation apiOperation = (ApiOperation) annotation;
                            Map<String, SwaggerPath.ApiMethod> methodMap = new HashMap<>();
                            apiMethod.setSummary(apiOperation.description());
                            apiMethod.setDescription(apiOperation.description());
                            apiMethod.addTag(apiOperation.tag());
                            methodMap.put(apiOperation.httpMethod(), apiMethod);
                            paths.put(apiOperation.url(), methodMap);
                            hasApi = true;
                        } else if (Params.class == annotationType) {
                            Params apiOperation = (Params) annotation;
                            Param[] params = apiOperation.value();
                            for (Param apiParam : params) {
                                apiMethod.addParameter(new SwaggerPath.Parameter(apiParam.name(), apiParam.description(), apiParam.required(), apiParam.dataType(), apiParam.format(), apiParam.defaultValue()));
                            }
                        } else if (Param.class == annotationType) {
                            Param apiParam = (Param) annotation;
                            apiMethod.addParameter(new SwaggerPath.Parameter(apiParam.name(), apiParam.description(), apiParam.required(), apiParam.dataType(), apiParam.format(), apiParam.defaultValue()));
                        }
                    }

                    if (hasApi && apiMethod.getParameters().size()==0 && method.getParameterTypes().length==1){
                        Class controllerParaClass = method.getParameterTypes()[0];
                        Table table = TableMapping.me().getTable(controllerParaClass);
                        LinkedHashMap<String,Object> dbConfig = DbConfig.getConfig(table.getName());

                        if(table != null){
                            for (String key:dbConfig.keySet()){

                                BaseConfig config = (BaseConfig) dbConfig.get(key);
                                String typeStr = table.getColumnType(key).getSimpleName();

                                apiMethod.addParameter(
                                        new SwaggerPath.Parameter(
                                                key,
                                                config.getTitle(),
                                                config.getRequired()!=null && config.getRequired()?true:false,
                                                typeStr,
                                                null,
                                                config.getDefault()
                                        )
                                );

                            }
                        }

                    }
                }
            }
        }

        if (classMap.size() > 0) {
            for (String key : classMap.keySet()) {
                doc.addTags(new SwaggerDoc.TagBean(key, classMap.get(key)));
            }
        }
        doc.setPaths(paths);
        return JSON.toJSONString(doc).replaceAll("\"defaultValue\"", "\"default\"");
    }
}
