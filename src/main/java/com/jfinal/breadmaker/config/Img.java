package com.jfinal.breadmaker.config;

/**
 * Created by liuandong on 2017/6/10.
 */
public class Img extends File {

    public static final Types type = Types.IMG;

    public Integer getWidth() {
        return _getInteger("width");
    }

    public void setWidth(Integer width) {
        put("width", width);
    }

    public Integer getHeight() {
        return _getInteger("height");
    }

    public void setHeight(Integer height) {
        put("height", height);
    }

    public Float getZoom() {
        return _getFloat("zoom");
    }

    public void setZoom(Float zoom) {
        put("zoom", zoom);
    }
}
