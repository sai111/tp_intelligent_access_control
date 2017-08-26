package com.jfinal.model;


import com.jfinal.plugin.activerecord.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by liuandong on 2017/6/14.
 */
public class MergeUtil {
    private List<?> data;

    public MergeUtil(List<?> data){
        this.data = data;
    }

    /**
     * 选定数据源
     * @param data
     * @return
     */
    public static MergeUtil list(List<?> data){
        return new MergeUtil(data);
    }

    /**
     * 通过dao绑定数据
     * @param data
     * @param dao
     * @param key
     * @return
     */
    public static MergeUtil merge(List<?> data, Model dao, String key){
        return new MergeUtil(data).merge(dao,key);
    }

    /**
     * 通过dao绑定数据
     * @param data
     * @param dao
     * @param config
     * @return
     */
    public static MergeUtil merge(List<?> data,Model dao,MergeConfig config){
        return new MergeUtil(data).merge(dao,config);
    }

    /**
     * 通过dao绑定数据
     * @param data
     * @param dao
     * @return
     */
    public static MergeUtil merge(List<?> data,Model dao){
        return new MergeUtil(data).merge(dao,(String) null);
    }

    /**
     * 通过dao绑定数据
     * @param dao
     * @param key
     * @return
     */
    public MergeUtil merge(Model dao,String key){
        if(dao instanceof iAssociation){
            MergeConfig config = ((iAssociation)dao).association(key);
            MergeData.merge(this.data ,dao.getClass(), config);
        }else {
            System.out.println("请对" + dao.getClass() + "继承iAssociation接口！");
        }
        return this;
    }

    /**
     * 通过dao绑定数据（从dao中取关联配置）
     * @param dao
     * @return
     */
    public MergeUtil merge(Model dao){
        if(dao instanceof iAssociation){
            MergeConfig config = ((iAssociation)dao).association(null);
            MergeData.merge(this.data ,dao.getClass(), config);
        }else {
            System.out.println("请对" + dao.getClass() + "继承iAssociation接口！");
        }
        return this;
    }

    /**
     * 通过dao绑定数据
     * @param dao
     * @param config 绑定配置
     * @return
     */
    public MergeUtil merge(Model dao,MergeConfig config){
        MergeData.merge(this.data ,dao.getClass(), config);
        return this;
    }

    /**
     * 绑定一组时间间距
     * @param key 时间戳的键名
     * @param timeKey 时间间距的键名
     * @param endStr 时间间距的后缀
     * @return
     */
    public MergeUtil mergeDistanceTime(String key, String timeKey, String endStr){
        MergeData.AddDistanceTime(this.data,key, timeKey, endStr);
        return this;
    }

    /**
     * 删除一些列
     * @param columns 列的键名
     * @return
     */
    public MergeUtil removeColumn(String... columns){
        MergeData.removeColumn(data,columns);
        return this;
    }

    /**
     * 合并一个map对象
     * @param key 主表的外键
     * @param newKey map绑定进的新键名
     * @param map 需要绑定的map
     * @return
     */
    public MergeUtil mergeMap(String key, String newKey,Map<Object,Object> map){
        MergeData.mergeMap(this.data,map,key,newKey);
        return this;
    }

    /**
     * 自动绑定
     * @return
     */
    public MergeUtil autoBind(){
        MergeData.autoBind(data);
        return this;
    }

    /**
     * 替换一个列名
     * @param valueKey 旧的键名
     * @param replaceKey 新的键名
     * @return
     */
    public MergeUtil replace(String valueKey, String replaceKey){
        MergeData.replace(data,valueKey,replaceKey);
        return this;
    }
}
