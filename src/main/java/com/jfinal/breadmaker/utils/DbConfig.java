package com.jfinal.breadmaker.utils;

import com.jfinal.breadmaker.config.BaseConfig;
import com.jfinal.breadmaker.config.Types;
import com.jfinal.kit.PathKit;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;
import tpson.app.WebConfig;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by liuandong on 2017/7/20.
 */
public class DbConfig {
    private static Map<String,LinkedHashMap<String,Object>> map = new HashMap<>();

    public static void main(String[] args) throws IOException {
        PathKit.setWebRootPath("/Users/liuandong/Documents/workspace/breadmaker/src/main/webapp");
        System.out.println(getConfig("wjw_admin"));
    }

    public static LinkedHashMap<String,Object> getConfig(String tableName) {

        if (map.containsKey(tableName)){
            return map.get(tableName);
        }else{
            try {
                LinkedHashMap<String,Object> result = new LinkedHashMap<>();
                Config cfg = new Config();
                Ini ini = null;

                if(WebConfig.is_release){
                    ini  = new Ini(Thread.currentThread().getContextClassLoader().getResourceAsStream("/db/" + tableName+".ini"));
                }else{
                    String fileDir = PathKit.getWebRootPath() + "/../resources/db/"+tableName+".ini";
                    File file = new File(fileDir);
                    ini  = new Ini(file);
                }

                cfg.setMultiSection(true);
                ini.setConfig(cfg);

                ini.forEach((name,section) ->{
                    result.put(name,getConfig(section,name));
                });

                map.put(tableName,result);
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    public static List getConfig(BmConfig config) {
        List result = new ArrayList();
        Map<String,Object> configMap = getConfig(config.getTableName());
        for(String column : config.getColumns()){
            if(configMap.containsKey(column)){
                result.add(configMap.get(column));
            }
        }
        return result;
    }

    private static BaseConfig getConfig(Profile.Section section,String keyName){

        try {
            Class c= Types.getClass(section.get("type"));
            BaseConfig config = (BaseConfig) c.newInstance();
            config.setKey(keyName);

            LinkedHashMap selectMap = new LinkedHashMap();

            for(String fieldName:section.keySet()) {
                if (fieldName.indexOf("value.") == 0){
                    selectMap.put(fieldName.substring(6),section.get(fieldName));
                }else{
                    config.put(fieldName,section.get(fieldName));
                }
            }
            if (selectMap.size() > 0){
                config.put("value",selectMap);
            }
            return config;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
