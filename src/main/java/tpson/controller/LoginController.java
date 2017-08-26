package tpson.controller;

import com.jfinal.aop.Clear;
import com.jfinal.authz.AuthzInterceptor;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.swagger.annotation.Api;
import com.jfinal.swagger.annotation.ApiOperation;
import com.jfinal.swagger.annotation.Param;
import com.jfinal.swagger.annotation.Params;
import tpson.service.UserService;
import tpson.utils.FrontReturnUtil;

/**
 * Created by liuandong on 2017/5/9.
 * 登录地址
 */
@Clear(AuthzInterceptor.class)
@Api(tag = "login", description = "登录")
public class LoginController extends Controller {

    public void index(){
        render("index.html");
    }

    @ApiOperation(url = "/login/data_login", tag = "login", httpMethod = "post", description = "登录")
    @Params({
            @Param(name = "username", description = "账号", required = true, dataType = "string", defaultValue = "admin"),
            @Param(name = "password", description = "密码", required = true, dataType = "string", defaultValue = "123456")
    })
    public void data_login(){
        String username = getPara("username");
        String password = getPara("password");

        try{
            if (UserService.login(username, password)){
                renderJson(Ret.ok());
            }else {
                renderJson(Ret.fail().set("error","用户名或密码错误!"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void logout(){

        String userName = (String)getSession().getAttribute("userName");
        if (userName != null && !userName.equals("")) {
            UserService.logout();
        }

        redirect("/login");
    }

}
