package tpson.model;

import com.jfinal.breadmaker.utils.IniGen;
import com.jfinal.kit.PathKit;

import javax.sql.DataSource;

/**
 * Created by liuandong on 2017/7/20.
 */
public class _genini {
    public static void main(String[] args) {
        DataSource ds = _generatorDao.getDataSource();
        String output = PathKit.getWebRootPath() + "/src/main/resources/db";
        IniGen gen = new IniGen(ds, output);
        gen.setRemovedTableNamePrefixes("tp_");
        gen.generate();
    }
}
