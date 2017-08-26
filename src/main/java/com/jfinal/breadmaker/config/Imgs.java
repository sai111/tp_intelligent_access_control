package com.jfinal.breadmaker.config;

/**
 * Created by liuandong on 2017/6/10.
 */
public class Imgs extends Img {

    public static final Types type = Types.IMGS;

    public Integer getMax() {
        return _getInteger("max");
    }

    public void setMax(Integer max) {
        put("max", max);
    }

    public Integer getMin() {
        return _getInteger("min");
    }

    public void setMin(Integer min) {
        put("min", min);
    }
}
