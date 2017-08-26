package com.jfinal.breadmaker.config;

import java.util.LinkedHashMap;

/**
 * Created by liuandong on 2017/6/10.
 */
public class Radio extends BaseConfig {

    public static final Types type = Types.RADIO;

    public LinkedHashMap getValue() {
        return (LinkedHashMap) get("value");
    }

    public void setValue(LinkedHashMap value) {
        put("value", value);
    }
}
