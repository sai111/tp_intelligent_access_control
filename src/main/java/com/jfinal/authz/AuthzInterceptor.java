package com.jfinal.authz;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import tpson.utils.FrontReturnUtil;

/**
 * Created by mayuefeng on 2017/7/10.
 */
public class AuthzInterceptor implements Interceptor {

    public void intercept(Invocation inv) {
        AuthzHandler ah = AuthzMap.getAuthzHandler(inv.getActionKey());
        String URL = inv.getController().getRequest().getRequestURI();
        String userName = (String) inv.getController().getSession().getAttribute("userName");

        if (AuthzMethod.isIgnoreUrl(URL)) {//忽略权限url过滤
            // TODO: 2017/8/9
        } else if (userName == null || userName.equals("")) {//登录验证
            if (StrKit.notBlank(AuthzMap.getLoginUrl())){
                inv.getController().redirect(AuthzMap.getLoginUrl());
            }else {
                inv.getController().renderJson(FrontReturnUtil.ERROR("请登录！"));
            }
            return;
        }else if (ah != null) {// 存在访问控制处理器。
            try {
                // 执行权限检查。
                if (!ah.assertAuthorized(inv.getController().getSession())) {
                    if (StrKit.notBlank(AuthzMap.getAuthzDenyUrl())){
                        inv.getController().redirect(AuthzMap.getAuthzDenyUrl());
                    }else {
                        inv.getController().renderJson(FrontReturnUtil.PERMISSION_DENY);
                    }
                    return;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        inv.invoke();
    }
}
