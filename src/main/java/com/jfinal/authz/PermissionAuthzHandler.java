package com.jfinal.authz;

import com.jfinal.authz.annotation.Logical;
import com.jfinal.authz.annotation.RequiresPermissions;
import com.jfinal.model.SqlBuilder;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import tpson.model.UserRoleAction;

import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayuefeng on 2017/7/10.
 */
public class PermissionAuthzHandler implements AuthzHandler{

    private final Annotation annotation;

    public PermissionAuthzHandler(Annotation annotation){
        this.annotation = annotation;
    }

    public boolean assertAuthorized(HttpSession session) {
        RequiresPermissions rrAnnotation = (RequiresPermissions) annotation;
        String[] permissions = rrAnnotation.value();
        List<String> currentPermissions = new ArrayList<>();
        Long currentRole = (Long) session.getAttribute("roleId");
        List<UserRoleAction> authorityLinks = new ArrayList<UserRoleAction>();
        Cache cache = Redis.use("web");
        if (cache.exists("roleAction")) {
            authorityLinks = cache.get("roleAction");
        }else {
            authorityLinks = SqlBuilder.dao(UserRoleAction.dao).allSelect();
            cache.setex("roleAction",60*60,authorityLinks);
        }
        for (UserRoleAction authorityLink:authorityLinks) {
            if (authorityLink.getRoleId() == currentRole) {
                currentPermissions.add(authorityLink.get("action_group_name") + "." + authorityLink.get("action_name"));
            }
        }
        int flag = 0;
        for (String permission : permissions) {
            for (String currentPermission : currentPermissions) {
                if(permission.equals(currentPermission)) {
                    flag++;
                    break;
                }
            }
        }
        if (rrAnnotation.logical() == Logical.AND && flag == permissions.length || rrAnnotation.logical() == Logical.OR && flag > 0) {
            return true;
        }else {
            return false;
        }
    }

}
