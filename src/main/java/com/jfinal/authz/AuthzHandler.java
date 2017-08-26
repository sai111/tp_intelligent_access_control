package com.jfinal.authz;

import javax.servlet.http.HttpSession;

/**
 * Created by mayuefeng on 2017/7/10.
 */
interface AuthzHandler {

    /**
     * 访问控制检查
     */
    public boolean assertAuthorized(HttpSession session);
}
