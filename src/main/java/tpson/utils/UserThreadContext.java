package tpson.utils;

import javax.servlet.http.HttpSession;

/**
 * Created by liuandong on 2017/7/6.
 */
public class UserThreadContext {
    private static ThreadLocal<HttpSession> threadLocal = new ThreadLocal<HttpSession>();

    public static HttpSession get(){
        return threadLocal.get();
    }

    public static void set(HttpSession session){
        threadLocal.set(session);
    }

    public static void remove(){
        threadLocal.remove();
    }
}
