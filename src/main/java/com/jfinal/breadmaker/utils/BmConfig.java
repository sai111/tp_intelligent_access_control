package com.jfinal.breadmaker.utils;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liuandong on 2017/7/28.
 */
public class BmConfig {
    private String tableName;
    private String[] columns;
    private Table _thisTable;
    private String action;
    private String type = "Index";

    public String getType() {
        return type;
    }

    public BmConfig setType(String type) {
        this.type = type;
        return this;
    }

    public BmConfig(Model model){
        _thisTable = TableMapping.me().getTable(model.getClass());
        tableName = _thisTable.getName();
    }

    public static BmConfig dao(Model model){
        return new BmConfig(model);
    }

    public String getAction() {
        return action;
    }

    public BmConfig setAction(String action) {
        this.action = action;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public BmConfig setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public BmConfig setColumns(String ...columns){
        if(columns.length == 1){
            this.columns = columns[0].split(",");
        }else{
            this.columns = columns;
        }
        return this;
    }

    public String[] getColumns(){
        return columns;
    }

    public BmConfig columnsExcept(String ...pair){
        Set<String> attrSet = new HashSet<>(_thisTable.getColumnTypeMap().keySet());
        for(String p : pair ){
            attrSet.remove(p);
        }

        List<String> columnList = new ArrayList<String>();
        attrSet.forEach(attr -> columnList.add(attr));
        columns = columnList.toArray(new String[columnList.size()]);

        return this;
    }
}
