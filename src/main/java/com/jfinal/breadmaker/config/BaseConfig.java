package com.jfinal.breadmaker.config;

import com.jfinal.kit.StrKit;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by liuandong on 2017/6/12.
 */
public class BaseConfig extends HashMap<String,Object> implements Serializable {

    public static final Types type = Types.CUSTOM;

    public String getKey() {
        return _getStr("key");
    }

    public void setKey(String key) {
        put("key", key);
    }

    public String getTitle() {
        return _getStr("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getDefault() {
        return _getStr("default");
    }

    public void setDefault(String defaultStr) {
        put("default", defaultStr);
    }

    public Boolean getRequired() {
        return _getBoolean("required");
    }

    public void setRequired(Boolean require) {
        put("required", require);
    }

    public String getType() {
        return _getStr("type");
    }

    public void setType(String type) {
        put("type", type);
    }

    protected String _getStr(String attr){
        return (String) get(attr);
    }

    protected Long _getLong(String attr){
        if (get(attr)!=null && StrKit.notBlank(get(attr).toString())){
            return Long.valueOf(get(attr).toString());
        }else{
            return null;
        }
    }

    protected Integer _getInteger(String attr){
        if (get(attr)!=null && StrKit.notBlank(get(attr).toString())){
            return Integer.valueOf(get(attr).toString());
        }else{
            return null;
        }
    }

    protected Float _getFloat(String attr){
        if (get(attr)!=null && StrKit.notBlank(get(attr).toString())){
            return Float.valueOf(get(attr).toString());
        }else{
            return null;
        }
    }

    protected Boolean _getBoolean(String attr){
        if (get(attr)!=null && StrKit.notBlank(get(attr).toString())){
            return Boolean.valueOf(get(attr).toString());
        }else{
            return null;
        }
    }

}
