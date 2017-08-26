package com.jfinal.authz;

/**
 * Created by mayuefeng on 2017/8/17.
 */
public class AuthzMethod {

    /**
     * 验证当前url是否包含忽略权限关键字
     * @param URL
     * @return
     */
    public static boolean isIgnoreUrl(String URL){
        String[] authzIgnoreUrlKeywords = AuthzMap.getAuthzIgnoreUrlKeywords();
        if (authzIgnoreUrlKeywords != null){
            for (String keywords: authzIgnoreUrlKeywords) {
                if (URL.contains(keywords)){
                    return true;
                }
            }
            return false;
        }
        return false;
    }

}
