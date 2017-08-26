package com.jfinal.model;

import com.jfinal.kit.StrKit;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liuandong on 2017/6/14.
 * 字符串转数组 数组转字符串
 */
public class ArrayStrUtil {
    public static Set<Long> str2LongSet(String ids){
        Set<Long> idSet = new HashSet<Long>();
        if (StrKit.isBlank(ids)){
            return idSet;
        }
        String [] idArr = ids.split(",");
        for (String i : idArr){
            try{
                idSet.add(Long.valueOf(i));
            }catch (Exception e) {
                ;
            }
        }
        return idSet;
    }

    public static int[] str2IntArray(String ids){
        List<Integer> idList = new ArrayList<Integer>();
        String [] idArr = ids.split(",");
        for (String i : idArr){
            try{
                idList.add(Integer.valueOf(i));
            }catch (Exception e) {
                ;
            }
        }

        int[] result = new int[idList.size()];
        for(int i = 0;i<idList.size(); i++){
            result[i] = idList.get(i);
        }

        return result;
    }

    public static long[] str2LongArray(String ids){
        List<Long> idList = new ArrayList<Long>();
        String [] idArr = ids.split(",");
        for (String i : idArr){
            try{
                idList.add(Long.valueOf(i));
            }catch (Exception e) {
                ;
            }
        }

        long[] result = new long[idList.size()];
        for(int i = 0;i<idList.size(); i++){
            result[i] = idList.get(i);
        }

        return result;
    }

    public static String join(Object data){
        if ( data instanceof List){
            return StringUtils.join(((List) data).toArray(),",");
        }else {
            return StringUtils.join(array2ArrayList(data),",");
        }
    }

    public static ArrayList<String> array2ArrayList(Object pair){
        ArrayList<String> l = new ArrayList<String>();
        if ( pair instanceof int[]){
            int [] arr = ( int[] ) pair ;
            if(arr.length > 0){
                for(int i : arr){
                    l.add(new Integer(i).toString());
                }

            }
        }else if ( pair instanceof long[]){
            long [] arr = ( long[] ) pair;
            if(arr.length > 0){
                for(long i : arr){
                    l.add(new Long(i).toString());
                }
            }
        }else if ( pair instanceof Set){
            Set<?> set = ( Set<?> ) pair;
            for (Object obj : set) {
                if ( obj instanceof Integer){
                    l.add( ((Integer)obj).toString() );
                }else if ( obj instanceof Long){
                    l.add(new Long((Long) obj).toString());
                }
            }
        }
        return l;
    }


}
