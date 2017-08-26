package com.jfinal.breadmaker.config;

/**
 * Created by liuandong on 2017/6/10.
 */
public class Text extends BaseConfig {
    public static final Types type = Types.TEXT;

    public String getValue() {
        return _getStr("value");
    }

    public void setValue(String value) {
        put("value", value);
    }

    public String getCheck() {
        return _getStr("check");
    }

    public void setCheck(String check) {
        put("check", check);
    }
    public String getPlaceholder() {
        return _getStr("placeholder");
    }

    public void setPlaceholder(String placeholder) {
        put("placeholder", placeholder);
    }

    public Integer getMaxLength() {
        return _getInteger("maxLength");
    }

    public void setMaxLength(Integer maxLength) {
        put("maxLength", maxLength);
    }

    public Integer getMinLength() {
        return _getInteger("minLength");
    }

    public void setMinLength(Integer minLength) {
        put("minLength", minLength);
    }
}
