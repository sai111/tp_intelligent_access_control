package com.jfinal.model;

import com.jfinal.kit.StrKit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuandong on 2017/5/23.
 */
public class FormFilter extends HashMap<String,String> {
    private int pageSize = 10; //分页大小
    private int pageNumber = 1; //当前页面

    public FormFilter(Map<String,String[]> requestParameterMap){
        requestParameterMap.entrySet().forEach( entry -> {
            put(entry.getKey(), entry.getValue()[0]);
        });

        //如果有页码
        if(containsKey("page_number") && !StrKit.isBlank(get("page_number"))){
            pageNumber = getInt("page_number");
        }

        //如果有分页大小
        if(containsKey("page_size") && !StrKit.isBlank(get("page_size"))){
            pageSize = getInt("page_size");
        }
    }

    //处理 a/1/b/2的情况
    public FormFilter(String requestUrl){
        String[] list = requestUrl.split("\\/");
        if (list.length > 1){
            for(int i=0; i<list.length; i+=2){
                put(list[i],list[i+1]);
            }
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPara(String name) {
        return get(name);
    }

    public String getPara(String name, String defaultValue) {
        String result = get(name);
        return result != null && !"".equals(result)?result:defaultValue;
    }

    public String getParaExcludeAll(String name, String excludeValue) {
        String result = get(name);
        return excludeValue.equals(result)?null:result;
    }


    private Integer toInt(String value, Integer defaultValue) {
        if(StrKit.isBlank(value)) {
            return defaultValue;
        } else {
            value = value.trim();
            return !value.startsWith("N") && !value.startsWith("n")?Integer.valueOf(Integer.parseInt(value)):Integer.valueOf(-Integer.parseInt(value.substring(1)));
        }
    }

    public Integer getInt(String name) {
        return this.toInt(get(name), (Integer)null);
    }

    public Integer getInt(String name, Integer defaultValue) {
        return this.toInt(get(name), defaultValue);
    }

    public Integer getIntExcludeAll(String name, int excludeValue) {
        Integer result = getInt(name);
        return result == null || result.intValue() == excludeValue ? null:result;
    }

    private Long toLong(String value, Long defaultValue) {
            if(StrKit.isBlank(value)) {
                return defaultValue;
            } else {
                value = value.trim();
                return !value.startsWith("N") && !value.startsWith("n")?Long.valueOf(Long.parseLong(value)):Long.valueOf(-Long.parseLong(value.substring(1)));
            }
    }

    public Long getLong(String name) {
        return this.toLong(get(name), (Long)null);
    }

    public Long getLong(String name, Long defaultValue) {
        return this.toLong(get(name), defaultValue);
    }

    public Long getLongExcludeAll(String name, long excludeValue) {
        Long result = getLong(name);
        return result == null || result.longValue() == excludeValue ? null : result;
    }

    public static int[] getIntIds(String ids){
        return ArrayStrUtil.str2IntArray(ids);
    }

    public static long[] getLongIds(String ids){
        return ArrayStrUtil.str2LongArray(ids);
    }
}
