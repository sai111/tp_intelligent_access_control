package com.jfinal.model;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by liuandong on 2017/6/9.
 * 合并配置
 */
public class MergeConfig implements Cloneable{
    private String fk; // 主表的外键
    private String pk; // 附表的主键
    private String[] columns; // 附表要列出的列名
    Map<String,String> aliasName = new Hashtable<String,String>(); //附表合并后的别名

    public MergeConfig(String fk, String pk){
        this.fk = fk;
        this.pk = pk;
    }

    public MergeConfig(String fk){
        this.fk = fk;
        this.pk = "id";
    }

    public void setFk(String fk) {
        this.fk = fk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getFk() {
        return fk;
    }

    public String getPk() {
        return pk;
    }

    public String[] getColumns() {
        if (columns == null && aliasName.size() > 0){
            return aliasName.keySet().toArray(new String[aliasName.size()]);
        }
        return columns;
    }

    public MergeConfig setColumns(String... columns) {
        this.columns = columns;
        return this;
    }

    public Map<String, String> getAliasName() {
        return aliasName;
    }

    public MergeConfig setAliasName(Map<String, String> aliasName) {
        this.aliasName = aliasName;
        return this;
    }

    public MergeConfig setAlias(String originalName, String newName) {
        aliasName.put(originalName, newName);
        return this;
    }

    protected MergeConfig clone(){
        return new MergeConfig(this.getFk(),this.getPk())
                .setColumns(this.columns)
                .setAliasName(this.aliasName);
    }


}
