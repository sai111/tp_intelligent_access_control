package tpson.intercepter;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import tpson.utils.UserThreadContext;

/**
 * Created by liuandong on 2017/7/6.
 */
public class SessionIntercept implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        UserThreadContext.set(inv.getController().getSession());
        inv.invoke();
        UserThreadContext.remove();
    }
}
