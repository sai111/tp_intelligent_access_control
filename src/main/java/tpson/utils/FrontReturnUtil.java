package tpson.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版权所有 杭州拓深科技
 * 由软件工程师吴清华在2017/5/3创建.
 */
public class FrontReturnUtil {

    public final static Map<String, Object> ERROR = new HashMap<String, Object>();
    public final static Map<String, Object> PARAM_ERROR = new HashMap<String, Object>();
    public final static Map<String, Object> PERMISSION_DENY = new HashMap<String, Object>();
    public final static Map<String, Object> SUCCESS = new HashMap<String, Object>();

    static {
        ERROR.put("code", 1);
        ERROR.put("error", "操作失败。");

        PARAM_ERROR.put("code", 1);
        PARAM_ERROR.put("error", "参数错误。");

        PERMISSION_DENY.put("code", 1);
        PERMISSION_DENY.put("error", "权限不足");

        SUCCESS.put("code", 0);
        SUCCESS.put("success", "操作成功。");
    }

    public static Map<String, Object> ERROR(String err)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 1);
        map.put("error", err);
        return map;
    }

    public static Map<String, Object> RET_ID(long id)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 2);
        map.put("id", id);
        return map;
    }

    public static Map<String, Object> DATA(Object o)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        if(o == null){
            map.put("data",new ArrayList<Object>());
            return map;
        }
        map.put("data", o);
        return map;
    }

    /**
     * 返回只有name和value的列 百度地图专用
     * @param list
     * @param nameKey
     * @param valueKey
     * @return
     */
    public static List<Record> NameAndValueList(List<Record> list, String nameKey, String valueKey){
        List<Record> result = new ArrayList<Record>();
        list.forEach(record -> {
            Record resultRecord = new Record();
            resultRecord.set("name",record.get(nameKey));
            resultRecord.set("value",record.get(valueKey));
            result.add(resultRecord);
        });
        return result;
    }

    /**
     * 自动把自连接的表组装成带children的数组
     * 注：带入的数组需要按parent_id,id 顺序排序
     * jstree专用
     * @param list
     * @param idKey list中的主键的键值
     * @param fidKey list中的外键的键值
     * @param textKey list中的text的键值
     * @param selectedKey list中的select的键值
     * @param lastKey list中的是否最后的键值 true|false
     * @param columnKey list中的需要列出的键值
     * @return
     */
    public static JSONArray treeList(List<Record> list,
                                     String idKey, String fidKey, String textKey, String selectedKey, String lastKey,
                                     String... columnKey){

        JSONArray array = new JSONArray();
        HashMap<Long,JSONObject> map = new HashMap<Long,JSONObject>();

        for (Record record : list){
            JSONObject json = new JSONObject();
            Long id = Long.valueOf(record.get(idKey).toString());
            Long fid = Long.valueOf(record.get(fidKey).toString());
            json.put("id", id);
            json.put("text", record.get(textKey));

            JSONObject state = new JSONObject();
            boolean checked = false;
            if (record.get(selectedKey) != null){
                String is_checked = record.get(selectedKey).toString();
                checked = (!StrKit.isBlank(is_checked) && (is_checked.equals("true") || is_checked.equals("1")));
            }
            state.put("selected", checked);
            state.put("opened", true);
            json.put("state", state);

            if (record.getBoolean(lastKey)){
                for (String column:columnKey){
                    json.put(column, record.get(column));
                }
            }
            map.put(id,json);

            //如果hash表中存在该元素，加入它的children中
            if (map.containsKey(fid)){
                //如果没有children
                if(!map.get(fid).containsKey("children")){
                    map.get(fid).put("children",new JSONArray());
                }
                ((JSONArray)map.get(fid).get("children")).add(json);
            //否则加入array
            }else{
                array.add(json);
            }
        }

        return array;
    }
}
