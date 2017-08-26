package tpson.controller.people;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.model.FormFilter;
import com.jfinal.swagger.annotation.Api;
import com.jfinal.swagger.annotation.ApiOperation;
import com.jfinal.swagger.annotation.Param;
import tpson.model.Visitor;
import tpson.service.VisitorService;
import tpson.utils.TimeUtil;

/**
 * Created by liuandong on 2017/8/23.
 * 访客管理
 */
@Api(tag = "visitor", description = "访客管理")
public class VisitorController extends Controller {

    @ApiOperation(url = "/visitor/data_page", tag = "visitor", httpMethod = "get", description = "访客分页")
    public void data_page(){
        renderJson(VisitorService.getPage(new FormFilter(getParaMap())));
    }

    @ApiOperation(url = "/visitor/data_add", tag = "visitor", httpMethod = "post", description = "访客增加")
    public void data_add(Visitor visitor){
        visitor.setStartTime(TimeUtil.getIntNow());
        visitor.save();
        renderJson(Ret.ok());
    }

    @ApiOperation(url = "/visitor/data_end", tag = "visitor", httpMethod = "post", description = "访客结束")
    public void data_end(Visitor visitor){
        visitor.setEndTime(TimeUtil.getIntNow());
        visitor.save();
        renderJson(Ret.ok());
    }

    @ApiOperation(url = "/visitor/data_update", tag = "visitor", httpMethod = "post", description = "访客更新")
    public void data_update(Visitor visitor){
        visitor.update();
        renderJson(Ret.ok());
    }

    @ApiOperation(url = "/visitor/data_delete", tag = "visitor", httpMethod = "post", description = "访客删除")
    @Param(name = "id", description = "id", required = true, dataType = "integer")
    public void data_delete(){
        Visitor.dao.deleteById(getParaToInt("id"));
        renderJson(Ret.ok());
    }
}
