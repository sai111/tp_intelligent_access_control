package tpson.app;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import java.io.File;
import java.lang.reflect.Field;

public class WebConfig {

    public static boolean is_debug;
    public static boolean is_release;

    public static int web_listen_port;

    public static String redis_ip;
    public static int redis_port;
    public static String redis_password;

    public static String db_ip;
    public static String db_user;
    public static String db_password;

    public static String imageMagick_path;

    public static void logParam() throws Exception{
        Field[] fields = WebConfig.class.getDeclaredFields();
        for(Field field : fields){
            System.out.println(field.getName() + ":" + field.get(field.getName()));
        }
    }

    /**
     * 利用反射把prop中的配置设置进对象
     * @param uri
     */
    public static void init(String uri){
        File file = new File(uri);
        if((file.exists() && file.isFile())){
            try {
                Prop prop = PropKit.use(file);
                Class c = WebConfig.class;
                Field[] fields = c.getDeclaredFields();
                for(Field f : fields){
                    if (prop.containsKey(f.getName())){
                        if (f.getType().equals(String.class)){
                            f.set(c,prop.get(f.getName()));
                        }else if(f.getType().equals(int.class)){
                            f.set(c,prop.getInt(f.getName()));
                        }else if(f.getType().equals(long.class)){
                            f.set(c,prop.getLong(f.getName()));
                        }else if(f.getType().equals(boolean.class)){
                            f.set(c,prop.getBoolean(f.getName()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void init(){
        init(System.getProperty("user.dir") + File.separator + "web.properties");
    }
}
