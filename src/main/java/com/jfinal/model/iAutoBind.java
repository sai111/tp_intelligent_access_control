package com.jfinal.model;

import java.util.List;

/**
 * Created by liuandong on 2017/7/16.
 */
public interface iAutoBind {
    public void bind(List<?> data, String columnName);
}
