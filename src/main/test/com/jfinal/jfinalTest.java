package com.jfinal;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import org.junit.Before;
import tpson.global.GlobalParam;
import tpson.model._MappingKit;
import tpson.utils.SystemUtil;

import java.io.File;

/**
 * Created by liuandong on 2017/7/7.
 */
public class jfinalTest {

    @Before
    public void setUp() throws Exception {
        SystemUtil.initSetting(PathKit.getWebRootPath()+ File.separatorChar + "conf/local.properties");
        String url = "jdbc:mysql://" + GlobalParam.db_ip + "/tpson_standalone?serverTimezone=GMT%2b8&autoReconnect=true&failOverReadOnly=false&useUnicode=true&characterEncoding=UTF-8";
        DruidPlugin druidPlugin = new DruidPlugin(url, GlobalParam.db_user, GlobalParam.db_password);
        druidPlugin.setInitialSize(1);
        druidPlugin.start();

        RedisPlugin redis = new RedisPlugin("web", GlobalParam.redis_ip, GlobalParam.redis_port);
        redis.start();

        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
        activeRecordPlugin.setShowSql(true);
        _MappingKit.mapping(activeRecordPlugin);
        activeRecordPlugin.start();
    }
}
