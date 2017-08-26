package com.jfinal.authz;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by mayuefeng on 2017/7/10.
 */
public class AuthzMap {

    /**
     * 登录界面
     */
    private static String loginUrl;

    /**
     * 权限不足跳转界面
     */
    private static String authzDenyUrl;

    /**
     * 忽略权限的路径关键字
     */

    private static String[] authzIgnoreUrlKeywords;

    /**
     * Session中保存的请求的Key值
     */
//    private static String SAVED_REQUEST_KEY = "jfinalShiroSavedRequest";

    /**
     * 用来记录那个action或者actionpath中是否有认证注解。
     */
    private static ConcurrentMap<String, AuthzHandler> authzMap = null;

    /**
     * 禁止初始化
     */
    private AuthzMap() {}

    static void init(ConcurrentMap<String, AuthzHandler> maps) {
        authzMap = maps;
    }

    static AuthzHandler getAuthzHandler(String actionKey){
		/*
		if(authzMaps.containsKey(controllerClassName)){
			return true;
		}*/
        return authzMap.get(actionKey);
    }

    public static final String getLoginUrl() {
        return loginUrl;
    }

    public static final void setLoginUrl(String loginUrl) {
        AuthzMap.loginUrl = loginUrl;
    }

    public static final String getAuthzDenyUrl() {
        return authzDenyUrl;
    }

    public static final void setAuthzDenyUrl(String authzDenyUrl) {
        AuthzMap.authzDenyUrl = authzDenyUrl;
    }

    public static final String[] getAuthzIgnoreUrlKeywords() {
        return authzIgnoreUrlKeywords;
    }

    public static final void setAuthzIgnoreUrlKeywords(String[] authzIgnoreUrlKeywords) {
        AuthzMap.authzIgnoreUrlKeywords = authzIgnoreUrlKeywords;
    }

    /**
     * Session中保存的请求的Key值
     * @return
     */
//    public static final String getSavedRequestKey(){
//        return SAVED_REQUEST_KEY;
//    }
}
