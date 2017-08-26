package com.jfinal.breadmaker.utils;

import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.plugin.activerecord.generator.TypeMapping;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuandong on 2017/6/12.
 */
public class IniGen {

    private String outputDir;
    private MetaBuilder metaBuilder;
    private DataSource dataSource;

    public IniGen(DataSource dataSource, String outputDir) {
        this.dataSource = dataSource;
        this.outputDir = outputDir;
        this.metaBuilder = new MetaBuilder(dataSource);
    }

    public void generate(List<TableMeta> tableMetas) {
        System.out.println("Generate ini ...");
        System.out.println("DB ini Output Dir: " + outputDir);
        for (TableMeta tableMeta : tableMetas) {
            genIniContent(tableMeta);
        }
        writeToFile(tableMetas);
    }

    protected void genIniContent(TableMeta tableMeta) {
        StringBuilder ret = new StringBuilder();
        for (ColumnMeta columnMeta : tableMeta.columnMetas) {
            ret.append("["+columnMeta.name+"]\n");

            ret.append(getColumnContent(columnMeta )+"\n");
            ret.append("\n");
        }
        tableMeta.baseModelContent = ret.toString();
    }

    protected String getColumnContent(ColumnMeta columnMeta){

        String title = "";
        if (!StrKit.isBlank(columnMeta.remarks)){
            title = columnMeta.remarks;
        }

        String _default = "";
        if (!StrKit.isBlank(columnMeta.defaultValue)){
            _default = columnMeta.defaultValue;
        }

        boolean isNull = false;
        if (!StrKit.isBlank(columnMeta.isNullable)){
            isNull = columnMeta.isNullable.equals("YES");
        }

        String require = (StrKit.isBlank(_default) && isNull ? "true" : "false");

        //主键
        if (columnMeta.isPrimaryKey.equals("PRI")){
            return "type = pk";
        }

        if (columnMeta.name.indexOf("time") >= 0){
            return "type = timestamp\n" +
                    "title = "+title+"\n" +
                    "require = "+require;
        }

        if (columnMeta.type.indexOf("CHAR") >= 0){
            return "type = text\n" +
                    "title = "+title+"\n" +
                    "placeholder = 请输入\n" +
                    "check = check\n" +
                    "default = "+_default+"\n" +
                    "require = "+ require +"\n" +
                    "maxLength = "+columnMeta.javaType+"\n" +
                    "minLength = 2";
        }

        if (columnMeta.type.indexOf("TEXT") >= 0){
            return "type = textarea\n" +
                    "title = "+title+"\n" +
                    "placeholder = 请输入\n" +
                    "check = check\n" +
                    "default = "+_default+"\n" +
                    "require = "+ require ;
        }

        if (columnMeta.type.indexOf("INT") >= 0){
            return "type = select\n" +
                    "title = "+title+"\n" +
                    "require = "+require+"\n" +
                    "value.1 = \n" +
                    "value.2 = ";
        }

        if (columnMeta.type.indexOf("BIT") >= 0){
            return "type = radio\n" +
                    "title = "+title+"\n" +
                    "require = "+require+"\n" +
                    "value.0 = false\n" +
                    "value.1 = true";
        }

        return "title = "+title;
    }

    protected void writeToFile(List<TableMeta> tableMetas) {
        try {
            for (TableMeta tableMeta : tableMetas) {
                writeToFile(tableMeta);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入
     */
    protected void writeToFile(TableMeta tableMeta) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String target = outputDir + File.separator + tableMeta.name + ".ini";

        File file = new File(target);
        if (file.exists()) {
            return ;	// 若 存在，不覆盖
        }

        FileWriter fw = new FileWriter(file);
        try {
            fw.write(tableMeta.baseModelContent);
        }
        finally {
            fw.close();
        }
    }

    /**
     * 设置需要被移除的表名前缀，仅用于生成 modelName 与  baseModelName
     * 例如表名  "osc_account"，移除前缀 "osc_" 后变为 "account"
     */
    public void setRemovedTableNamePrefixes(String... removedTableNamePrefixes) {
        metaBuilder.setRemovedTableNamePrefixes(removedTableNamePrefixes);
    }

    protected void rebuildColumnMetas(List<TableMeta> tableMetas) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData dbMeta = conn.getMetaData();
            TypeMapping typeMapping = new TypeMapping();


            for (TableMeta tableMeta : tableMetas) {
                // 重建整个 TableMeta.columnMetas
                tableMeta.columnMetas = new ArrayList<ColumnMeta>();
                // 通过查看 dbMeta.getColumns(...) 源码注释，还可以获取到更多 meta data
                ResultSet rs = dbMeta.getColumns(conn.getCatalog(), null, tableMeta.name, null);
                ResultSetMetaData rsmd = rs.getMetaData();
                while (rs.next()) {
                    ColumnMeta columnMeta = new ColumnMeta();
                    columnMeta.name = rs.getString("COLUMN_NAME");			// 名称

                    columnMeta.type = rs.getString("TYPE_NAME");			// 类型
                    if (columnMeta.type == null) {
                        columnMeta.type = "";
                    }

                    int columnSize = rs.getInt("COLUMN_SIZE");
                    if (columnSize > 0) {
                        columnMeta.javaType = "" + columnSize;
                    }

                    columnMeta.isNullable = rs.getString("IS_NULLABLE");	// 是否允许 NULL 值
                    if (columnMeta.isNullable == null) {
                        columnMeta.isNullable = "";
                    }

                    columnMeta.isPrimaryKey = "   ";
                    String[] keys = tableMeta.primaryKey.split(",");
                    for (String key : keys) {
                        if (key.equalsIgnoreCase(columnMeta.name)) {
                            columnMeta.isPrimaryKey = "PRI";
                            break;
                        }
                    }

                    columnMeta.defaultValue = rs.getString("COLUMN_DEF");	// 默认值
                    if (columnMeta.defaultValue == null) {
                        columnMeta.defaultValue = "";
                    }

                    columnMeta.remarks = rs.getString("REMARKS");			// 备注
                    if (columnMeta.remarks == null) {
                        columnMeta.remarks = "";
                    }

                    columnMeta.remarks = rs.getString("REMARKS");

                    if (tableMeta.colNameMaxLen < columnMeta.name.length()) {
                        tableMeta.colNameMaxLen = columnMeta.name.length();
                    }
                    if (tableMeta.colTypeMaxLen < columnMeta.type.length()) {
                        tableMeta.colTypeMaxLen = columnMeta.type.length();
                    }
                    if (tableMeta.colDefaultValueMaxLen < columnMeta.defaultValue.length()) {
                        tableMeta.colDefaultValueMaxLen = columnMeta.defaultValue.length();
                    }

                    tableMeta.columnMetas.add(columnMeta);
                }
                rs.close();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (conn != null) {
                try {conn.close();} catch (SQLException e) {
                    LogKit.error(e.getMessage(), e);}
            }
        }
    }

    public void generate() {
        List<TableMeta> tableMetas = metaBuilder.build();
        if (tableMetas.size() == 0) {
            System.out.println("TableMeta 数量为 0，不生成任何文件");
            return;
        }

        rebuildColumnMetas(tableMetas);

        generate(tableMetas);
    }

}
