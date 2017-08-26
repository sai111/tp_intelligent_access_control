package tpson.model;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

/**
 * 版权所有 杭州拓深科技
 * 由软件工程师吴清华在2017/5/3创建.
 */
public class _generatorDao {

    public static DataSource getDataSource(){
        String url = "jdbc:mysql://192.168.1.2:3306/tp_iac?serverTimezone=GMT%2b8&autoReconnect=true&failOverReadOnly=false&useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String password = "tpson102304";
        DruidPlugin druidPlugin = new DruidPlugin(url,user,password);
        druidPlugin.setInitialSize(1);
        druidPlugin.start();
        return druidPlugin.getDataSource();
    }

    public static void main(String[] args){
        // base model 所使用的包名
        String baseModelPackageName = "tpson.model.base";
        // base model 文件保存路径
        System.out.println(PathKit.getWebRootPath());
        String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/tpson/model/base";
        System.out.println("model url:" + baseModelOutputDir);
        // model 所使用的包名 (MappingKit 默认使用的包名)
        String modelPackageName = "tpson.model";
        // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
        String modelOutputDir = baseModelOutputDir + "/..";
        // 创建生成器
        Generator gernerator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
        // 添加不需要生成的表名
        // gernerator.addExcludedTable("adv");
        // 设置是否在 Model 中生成 dao 对象
        gernerator.setGenerateDaoInModel(true);
        // 设置是否生成字典文件
        gernerator.setGenerateDataDictionary(false);
        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
        gernerator.setRemovedTableNamePrefixes("tp_");
        // 生成
        gernerator.generate();
    }

}
