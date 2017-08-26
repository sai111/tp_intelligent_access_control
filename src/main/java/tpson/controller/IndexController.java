package tpson.controller;

import com.jfinal.core.Controller;
import com.jfinal.swagger.annotation.Api;
import com.jfinal.swagger.annotation.ApiOperation;
import com.jfinal.swagger.annotation.Param;
import com.jfinal.swagger.annotation.Params;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liuandong on 2017/8/23.
 */
public class IndexController extends Controller {
    public void index(){
        render("/dist/index.html");
    }
}
