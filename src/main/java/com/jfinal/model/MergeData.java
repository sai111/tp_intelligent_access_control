package com.jfinal.model;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import org.apache.commons.lang3.StringUtils;
import tpson.utils.TimeUtil;

import java.util.*;

/**
 * 数据合并
 * Created by liuandong on 2017/5/10.
 */
public class MergeData {
    /**
     * 对一个model，record对象通过外键进行数据合并
     * @param data 合并的数据
     * @param modelClass 要合并model的class
     * @param config 合并配置
     */
    public static void merge(List<?> data, Class<? extends Model> modelClass, MergeConfig config){
        Set<Object> ids = _getIds(data,config.getFk());
        String tableName = TableMapping.me().getTable(modelClass).getName();
        if (ids == null || ids.size()==0) return;
        HashMap<Long,Record> map = _getTabMap(tableName,config.getPk(), ArrayStrUtil.array2ArrayList(ids));

        _mergeModelOrRecordAndRecordMap(data,config.getFk(),map,config.getColumns(),config.getAliasName());
    }

    /**
     * 合并一个map对象
     * @param data
     * @param map
     * @param pidKey
     * @param mergeKey
     */
    public static void mergeMap(List<?> data, Map<Object,Object> map, String pidKey, String mergeKey){
        for(int i = 0; i<data.size(); i++){
            _setRename(data.get(i), mergeKey, map.get(_get(data.get(i), pidKey)));
        }
    }

    /**
     * 替换一个键名
     * @param data
     * @param valueKey
     * @param replaceKey
     */
    public static void replace(List<?> data,String valueKey, String replaceKey){
        data.forEach(d -> {
            _set(d,replaceKey,_get(d,valueKey));
            _remove(d,valueKey);
        });
    }

    /**
     * 删除掉列
     * @param data
     * @param column
     */
    public static void removeColumn(List<?> data, String... column){
        for(int i = 0; i<data.size(); i++){
            for(String col : column){
                _remove(data.get(i),col);
            }
        }
    }

    private static Set<Object> _getIds(List<?> data, String key){
        Set<Object> ids = new HashSet<Object>();
        int columnCount = 0;
        for (Object obj: data) {
            if (obj instanceof Record){
                Record record = (Record) obj;
                if (record.get(key) != null && StrKit.notBlank(record.get(key).toString())){
                    ids.add(record.get(key));
                    columnCount++;
                }
            }else if(obj instanceof Model){
                Model model = (Model) obj;
                if (model.get(key) != null && StrKit.notBlank(model.get(key).toString())){
                    ids.add(model.get(key));
                    columnCount++;
                }
            }
        }
        return columnCount > 0 ? ids : null;
    }

    private static Long _getLongId(Object obj, String key){
        if (obj instanceof Record && ((Record) obj).get(key) != null){
            Record record = (Record) obj;
            if (record.get(key) != null && StrKit.notBlank(record.get(key).toString())){
                return new Long(record.get(key).toString());
            }
        }else if(obj instanceof Model && ((Model) obj).get(key) != null){
            Model model = (Model) obj;
            if (model.get(key) != null && StrKit.notBlank(model.get(key).toString())){
                return new Long(model.get(key).toString());
            }
        }
        return null;
    }

    private static Object _get(Object obj, String column){
        if (obj instanceof Record){
            Record record = (Record) obj;
            return record.get(column);
        }else if(obj instanceof Model){
            Model model = (Model) obj;
            return model.get(column);
        }
        return null;
    }

    private static Object _remove(Object obj, String column){
        if (obj instanceof Record){
            Record record = (Record) obj;
            return record.remove(column);
        }else if(obj instanceof Model){
            Model model = (Model) obj;
            return model.remove(column);
        }
        return null;
    }

    private static void _set(Object obj, String column, Object value){
        if (obj instanceof Record){
            Record record = (Record) obj;
            record.set(column,value);
        }else if(obj instanceof Model){
            Model model = (Model) obj;
            model.put(column,value);
        }
    }

    private static void _setRename(Object obj, String column, Object value){
        if (obj instanceof Record){
            Record record = (Record) obj;
            int index = 2;
            String columnName = column;
            while (record.get(columnName)!=null){
                columnName = column + index++;
            }
            record.set(columnName, value);
        }else if(obj instanceof Model){
            Model model = (Model) obj;
            int index = 2;
            String columnName = column;
            while (model.get(columnName)!=null){
                columnName = column + index++;
            }
            model.put(columnName, value);
        }
    }

    private static HashMap<Long,Record> _getTabMap(String tableName,String pk,List<String> ids){
        String sql = "select * from " + tableName +
                " where " + pk + " in(" +
                StringUtils.join(ids,",")
                +") ";

        List<Record> list = Db.find(sql);
        HashMap<Long,Record> map = new HashMap<Long,Record>();
        list.forEach(record -> map.put(Long.parseLong(record.get(pk).toString()),record));
        return map;
    }

    /**
     * 对两个model数据合并
     * @param data 主数据
     * @param data2 要合并的数据
     * @param config 配置
     */
    public static void mergeData(List<?> data, List<?> data2, MergeConfig config){
        HashMap<Long,Record> map = new HashMap<Long,Record>();

        data2.forEach(model ->{
            map.put(_getLongId(model,config.getPk()), model instanceof Model?((Model)model).toRecord():(Record) model);
        });

        _mergeModelOrRecordAndRecordMap(data,config.getFk(),map,config.getColumns(),config.getAliasName());
    }

    private static void _mergeModelOrRecordAndRecordMap(List<?> data, String pidKey, HashMap<Long,Record> map, String[] columns, Map<String,String> aliasName){

        if (columns == null || columns.length==0){
            System.out.println("!!!" + "请设置要绑定的列 "+ pidKey);
            return;
        }

        for(int i = 0; i<data.size(); i++){
            Long tempValue = _getLongId(data.get(i), pidKey);
            if (tempValue == null) {
                for (String col: columns) {
                    _setRename(data.get(i),col, null);
                }
                continue;
            }
            Record record = map.get(tempValue);
            for (String col: columns) {
                if(aliasName!=null && aliasName.containsKey(col)){
                    if (record == null){
                        _set(data.get(i),aliasName.get(col),null);
                    }else{
                        _set(data.get(i),aliasName.get(col),record.get(col));
                    }
                }else{
                    if (record == null){
                        _setRename(data.get(i),col, null);
                    }else{
                        _setRename(data.get(i),col, record.get(col));
                    }
                }
            }
        }
    }

    /**
     * 增加一个时间间距
     * @param list
     * @param key
     * @param timeKey 时间键名
     * @param endStr 尾部字符串
     */
    public static void AddDistanceTime(List<?> list, String key, String timeKey, String endStr){
        long now = TimeUtil.getNow();
        for (int i = 0; i<list.size(); i++){
            Object obj = list.get(i);
            Long time = _getLongId(obj, key);
            if(time != null){
                String distance = TimeUtil.getDistanceTimeStr(time,now);
                _set(obj, timeKey, distance+endStr);
            }else {
                _set(obj, timeKey, "");
            }
        }
    }

    /**
     * 自动绑定
     * @param data
     */
    public static void autoBind(List<?> data){
        if(data.size() > 0)
        if(data.get(0) instanceof iAutoBind && data.get(0) instanceof Model){
            iAutoBind bind = (iAutoBind) data.get(0);
            Model model = (Model) data.get(0);
            for (String name : model._getAttrNames()){
                bind.bind(data,name);
            }
        }else{
            System.out.println(data.get(0).getClass() + "需要绑定iAutoBind接口！");
        }
    }

}