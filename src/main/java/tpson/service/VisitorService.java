package tpson.service;

import com.jfinal.model.FormFilter;
import com.jfinal.model.SqlBuilder;
import com.jfinal.plugin.activerecord.Page;
import tpson.model.Visitor;

/**
 * Created by liuandong on 2017/8/23.
 */
public class VisitorService {
    public static Page<Visitor> getPage(FormFilter filter){
        Page<Visitor> result = SqlBuilder.dao(Visitor.dao)
                .orderBy("start_time")
                .page(filter);

        return result;
    }
}
