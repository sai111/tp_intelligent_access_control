package com.jfinal.authz;

import com.jfinal.authz.annotation.RequiresRoles;
import com.jfinal.model.SqlBuilder;
import tpson.model.UserRole;

import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by mayuefeng on 2017/7/10.
 */
public class RoleAuthzHandler implements AuthzHandler {

    private final Annotation annotation;

    public RoleAuthzHandler(Annotation annotation){
        this.annotation = annotation;
    }

    public boolean assertAuthorized(HttpSession session) {
        RequiresRoles rrAnnotation = (RequiresRoles) annotation;
        String role = rrAnnotation.value();
        Long currentRole = (Long) session.getAttribute("roleId");
        String currentRoleName = null;
        List<UserRole> userRoles = SqlBuilder.dao(UserRole.dao).select();
        for (UserRole userRole : userRoles) {
            if (userRole.getId() == currentRole) {
                currentRoleName = userRole.getName();
            }
        }
        if (role != null && role.equals(currentRoleName)) {
            return true;
        } else {
            return false;
        }

    }

}
