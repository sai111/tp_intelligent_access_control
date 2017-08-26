package com.jfinal.authz;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by mayuefeng on 2017/7/10.
 */
public class CompositeAuthzHandler implements AuthzHandler {
    private final List<AuthzHandler> authzHandlers;

    public CompositeAuthzHandler(List<AuthzHandler> authzHandlers){
        this.authzHandlers = authzHandlers;
    }

    public boolean assertAuthorized(HttpSession session){
        boolean flag = true;
        for(AuthzHandler authzHandler : authzHandlers){
            flag = authzHandler.assertAuthorized(session) && flag;
        }
        return flag;
    }
}
