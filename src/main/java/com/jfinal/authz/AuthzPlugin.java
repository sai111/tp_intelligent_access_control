package com.jfinal.authz;

import com.jfinal.authz.annotation.ClearAuthz;
import com.jfinal.authz.annotation.RequiresPermissions;
import com.jfinal.authz.annotation.RequiresRoles;
import com.jfinal.config.Routes;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.IPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by mayuefeng on 2017/7/10.
 */
public class AuthzPlugin implements IPlugin {

    /**
     * 登录界面
     */
    private  String loginUrl;

    /**
     * 权限不足跳转界面
     */
    private  String authzDenyUrl;

    /**
     * 忽略权限的路径关键字
     */
    private  String[] authzIgnoreUrlKeywords;

    private static String SLASH = "/";

    /**
     * 几种访问控制注解
     */
    private static final Class<? extends Annotation>[] AUTHZ_ANNOTATION_CLASSES = new Class[] {
            RequiresPermissions.class, RequiresRoles.class};

    /**
     * 路由设定
     */
    private static Routes routes;

    /**
     * 构造函数
     * @param routes 路由设定
     */
    public AuthzPlugin(Routes routes){
        this.routes = routes;
    }

    /**
     * 停止插件
     */
    public boolean stop() {
        return true;
    }


    public boolean start() {
        Set<String> excludedMethodName = buildExcludedMethodName();
        ConcurrentMap<String, AuthzHandler> authzMap = new ConcurrentHashMap<String, AuthzHandler>();
        //逐个访问所有注册的Controller，解析Controller及action上的所有注解。
        //并依据这些注解，actionKey提前构建好权限检查处理器。
        for (Routes routes : getRoutesList()) {
            for (Routes.Route route : routes.getRouteItemList()) {
                Class controllerClass = route.getControllerClass();
                String controllerKey = route.getControllerKey();

                // 获取Controller的所有注解。
                List<Annotation> controllerAnnotations = getAuthzAnnotations(controllerClass);
                // 逐个遍历方法。
                Method[] methods = controllerClass.getMethods();
                for (Method method : methods) {
                    //排除掉Controller基类的所有方法，并且只关注没有参数的Action方法。
                    if (!excludedMethodName.contains(method.getName())
                            && method.getParameterTypes().length == 0) {
                        //若该方法上存在ClearAuthz注解，则对该action不进行访问控制检查。
                        if (isClearAuthzAnnotationPresent(method)) {
                            continue;
                        }
                        //获取方法的所有注解。
                        List<Annotation> methodAnnotations = getAuthzAnnotations(method);
                        //依据Controller的注解和方法的注解来生成访问控制处理器。
                        AuthzHandler authzHandler = createAuthzHandler(
                                controllerAnnotations, methodAnnotations);
                        //生成访问控制处理器成功。
                        if (authzHandler != null) {
                            //构建ActionKey，参考ActionMapping中实现
                            String actionKey = createActionKey(controllerClass, method, controllerKey);
                            //添加映射
                            authzMap.put(actionKey, authzHandler);
                        }
                    }
                }
            }
        }
        //注入到PermissionMap类中。PermissionMap类以单例模式运行。
        AuthzMap.init(authzMap);
        /**
         * 设定登录，登录成功，未授权等url地址
         */
        AuthzMap.setLoginUrl(loginUrl);
        AuthzMap.setAuthzDenyUrl(authzDenyUrl);
        AuthzMap.setAuthzIgnoreUrlKeywords(authzIgnoreUrlKeywords);
        return true;
    }


    /**
     * 从Controller方法中构建出需要排除的方法列表
     * @return
     */
    private static Set<String> buildExcludedMethodName() {
        Set<String> excludedMethodName = new HashSet<String>();
        Method[] methods = Controller.class.getMethods();
        for (Method m : methods) {
            if (m.getParameterTypes().length == 0)
                excludedMethodName.add(m.getName());
        }
        return excludedMethodName;
    }

    /**
     * 返回该方法的所有访问控制注解
     *
     * @param method
     * @return
     */
    private static List<Annotation> getAuthzAnnotations(Method method) {
        List<Annotation> annotations = new ArrayList<Annotation>();
        for (Class<? extends Annotation> annClass : AUTHZ_ANNOTATION_CLASSES) {
            Annotation a = method.getAnnotation(annClass);
            if (a != null) {
                annotations.add(a);
            }
        }
        return annotations;
    }

    /**
     * 返回该Controller的所有访问控制注解
     *
     */
    private static List<Annotation> getAuthzAnnotations(
            Class<? extends Controller> targetClass) {
        List<Annotation> annotations = new ArrayList<Annotation>();
        for (Class<? extends Annotation> annClass : AUTHZ_ANNOTATION_CLASSES) {
            Annotation a = targetClass.getAnnotation(annClass);
            if (a != null) {
                annotations.add(a);
            }
        }
        return annotations;
    }

    /**
     * 依据Controller的注解和方法的注解来生成访问控制处理器。
     * @param controllerAnnotations  Controller的注解
     * @param methodAnnotations 方法的注解
     * @return 访问控制处理器
     */
    private static AuthzHandler createAuthzHandler(
            List<Annotation> controllerAnnotations,
            List<Annotation> methodAnnotations) {

        //没有注解
        if (controllerAnnotations.size() == 0 && methodAnnotations.size() == 0) {
            return null;
        }
        //至少有一个注解
        List<AuthzHandler> authzHandlers = new ArrayList<AuthzHandler>(2);
        for (int index = 0; index < 2; index++) {
            authzHandlers.add(null);
        }

        // 逐个扫描注解，若是相应的注解则在相应的位置赋值。
        scanAnnotation(authzHandlers, controllerAnnotations);
        // 逐个扫描注解，若是相应的注解则在相应的位置赋值。函数的注解优先级高于Controller
        scanAnnotation(authzHandlers, methodAnnotations);

        // 去除空值
        List<AuthzHandler> finalAuthzHandlers = new ArrayList<AuthzHandler>();
        for (AuthzHandler a : authzHandlers) {
            if (a != null) {
                finalAuthzHandlers.add(a);
            }
        }
        authzHandlers = null;
        // 存在多个，则构建组合AuthzHandler
        if (finalAuthzHandlers.size() > 1) {
            return new CompositeAuthzHandler(finalAuthzHandlers);
        }
        // 一个的话直接返回
        return finalAuthzHandlers.get(0);
    }


    /**
     * 逐个扫描注解，若是相应的注解则在相应的位置赋值。
     * 注解的处理是有顺序的，依次为RequiresRoles，RequiresPermissions，
     * RequiresAuthentication，RequiresUser，RequiresGuest
     *
     * @param authzArray
     * @param annotations
     */
    private static void scanAnnotation(List<AuthzHandler> authzArray,
                                List<Annotation> annotations) {
        if (null == annotations || 0 == annotations.size()) {
            return;
        }
        for (Annotation a : annotations) {
            if (a instanceof RequiresRoles) {
                authzArray.set(0, new RoleAuthzHandler(a));
            } else if (a instanceof RequiresPermissions) {
                authzArray.set(1, new PermissionAuthzHandler(a));
            }
        }
    }


    /**
     * 构建actionkey，参考ActionMapping中的实现。
     *
     * @param controllerClass
     * @param method
     * @param controllerKey
     * @return
     */
    private static String createActionKey(Class<? extends Controller> controllerClass,
                                   Method method, String controllerKey) {
        String methodName = method.getName();
        String actionKey = "";

        ActionKey ak = method.getAnnotation(ActionKey.class);
        if (ak != null) {
            actionKey = ak.value().trim();
            if ("".equals(actionKey))
                throw new IllegalArgumentException(controllerClass.getName() + "." + methodName + "(): The argument of ActionKey can not be blank.");
            if (!actionKey.startsWith(SLASH))
                actionKey = SLASH + actionKey;
        }
        else if (methodName.equals("index")) {
            actionKey = controllerKey;
        }
        else {
            actionKey = controllerKey.equals(SLASH) ? SLASH + methodName : controllerKey + SLASH + methodName;
        }
        return actionKey;
    }


    private static List<Routes> getRoutesList() {
        List<Routes> routesList = Routes.getRoutesList();
        List<Routes> ret = new ArrayList<Routes>(routesList.size() + 1);
        ret.add(routes);
        ret.addAll(routesList);
        return ret;
    }

    /**
     * 该方法上是否有ClearShiro注解
     * @param method
     * @return
     */
    private boolean isClearAuthzAnnotationPresent(Method method) {
        Annotation a = method.getAnnotation(ClearAuthz.class);
        if (a != null) {
            return true;
        }
        return false;
    }

    /**
     * 登录连接
     * @param loginUrl
     */
    public final void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    /**
     * 未授权连接
     * @param authzDenyUrl
     */
    public final void setAuthzDenyUrl(String authzDenyUrl) {
        this.authzDenyUrl = authzDenyUrl;
    }

    /**
     * 未授权连接
     * @param authzIgnoreUrlKeywords
     */
    public final void setAuthzIgnoreUrlKeywords(String[] authzIgnoreUrlKeywords) {
        this.authzIgnoreUrlKeywords = authzIgnoreUrlKeywords;
    }
}
