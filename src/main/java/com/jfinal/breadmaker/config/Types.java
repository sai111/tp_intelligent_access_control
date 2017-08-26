package com.jfinal.breadmaker.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuandong on 2017/6/10.
 */
public enum Types {
    //文字类
    TEXT(1),
    TEXTAREA(2),
    EDITOR(3),
    PASSWORD(4),

    //单选类
    RADIO(10),
    SELECT(11),

    //多选类
    CHECKBOX(20),

    //多媒体类
    FILE(30),
    FILES(31),
    IMG(32),
    IMGS(33),

    JSON(40),

    //时间类
    DATE(50),
    TIMESTAMP(51),

    //主键、外键
    PK(60),
    FK(61),
    CUSTOM(100);

    private final int value;

    private Types(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static final Map<Integer,String> map = new HashMap<Integer,String>(){{
        put(1, "text");
        put(2, "textarea");
        put(3, "editor");
        put(4, "password");
        put(10, "radio");
        put(11, "select");
        put(20, "checkbox");
        put(30, "file");
        put(31, "files");
        put(32, "img");
        put(33, "imgs");
        put(40, "json");
        put(50, "date");
        put(51, "timestamp");
        put(60, "pk");
        put(70, "fk");
        put(100, "custom");
    }};

    public String getStrValue() {
        return map.get(value);
    }

    public static Class getClass(String type){
        switch (type) {
            case "checkbox":return CheckBox.class;
            case "date":return Date.class;
            case "editor":return Editor.class;
            case "file":return com.jfinal.breadmaker.config.File.class;
            case "files":return Files.class;
            case "fk":return Fk.class;
            case "img":return Img.class;
            case "imgs":return Imgs.class;
            case "json":return Json.class;
            case "password":return Password.class;
            case "pk":return Pk.class;
            case "radio":return Radio.class;
            case "select":return Select.class;
            case "text":return Text.class;
            case "textarea":return TextArea.class;
            case "timestamp":return Timestamp.class;
            default:return BaseConfig.class;
        }
    }
}
