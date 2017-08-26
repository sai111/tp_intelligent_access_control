package tpson.service;

import com.jfinal.kit.StrKit;
import com.jfinal.model.ArrayStrUtil;
import com.jfinal.model.FormFilter;
import com.jfinal.model.SqlBuilder;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import sun.misc.BASE64Encoder;
import tpson.app.AppConfig;
import tpson.model.User;
import tpson.model.UserRole;
import tpson.utils.TimeUtil;
import tpson.utils.UserThreadContext;

import java.security.MessageDigest;
import java.util.Random;

/**
 * 用户服务
 * Created by liuandong on 2017/5/24.
 */
public class UserService {

    public static Page<User> getPage(FormFilter filter){
        return SqlBuilder.dao(User.dao)
                .columnsExcept("password","salt","delete_time")
                .where("is_delete", 0)
                .orderBy("id", "desc")
                .page(filter);
    }

    /**
     * 将客户输入的密码加密
     * @param inputPasswd 客户输入的密码
     * @param salt 盐
     * @return 加密后的字符串
     */
    public static String getPassword(String salt, String inputPasswd){
        String pwd = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5", "SUN");
            BASE64Encoder b64Encoder = new BASE64Encoder();
            md.reset();
            md.update(salt.getBytes("UTF-8"));
            md.update(inputPasswd.getBytes("UTF-8"));
            byte[] bys = md.digest();
            pwd = b64Encoder.encode(bys);
        }catch(Exception ex){
            ex.printStackTrace();
            pwd = "";
        }

        return pwd;
    }

    /**
     * 校验密码
     * @param password
     * @param user
     * @return
     */
    public static boolean matchPassword(String password, User user){
        String passTemp = getPassword(user.getSalt(), password);
        return (!StrKit.isBlank(passTemp)) && passTemp.equals(user.getPassword());
    }

    /**
     * 获取密码盐
     * 返回指定长度的盐(ASCII码)
     * @return
     */
    public static String getSalt(){
        //notice: 此处用6是为了开发阶段迁移数据库
        byte[] salt = new byte[6];
        Random rand = new Random();
        for(int i=0; i<6; i++){
            salt[i] = (byte) ((rand.nextInt('~'-'!')+'!') & 0x007f);
        }
        return new String(salt).trim();
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     * 添加上线当前时间和上线的方式(web)
     */
    public static boolean login(String userName, String password) {
        User user = SqlBuilder.dao(User.dao)
                .where("username", userName)
                .findFirst();
        if (user == null){
            return false;
        }else if(matchPassword(password, user) && !user.getIsDelete() && !user.getIsDisable()) {
            UserThreadContext.get().setAttribute("userId", user.getId());
            UserThreadContext.get().setAttribute("userName", user.getUsername());
            UserThreadContext.get().setAttribute("name", user.getName());
            UserThreadContext.get().setAttribute("roleId", user.getRoleId());
//            UserThreadContext.get().setAttribute("companyId", user.getCompanyId());
            user.setLoginTimes(user.getLoginTimes() + 1);
            user.update();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 登出
     * @return
     */
    public static void logout() {
        UserThreadContext.get().removeAttribute("userId");
        UserThreadContext.get().removeAttribute("userName");
        UserThreadContext.get().removeAttribute("name");
        UserThreadContext.get().removeAttribute("roleId");
    }

    //保存用户
    public static boolean save(User user){
        String salt = getSalt();
        user.setSalt(salt);
        user.setPassword(getPassword(salt, user.getPassword()));
        long now = TimeUtil.getNow();
        user.setAddTime(now);
        user.setUpdateTime(now);
        return user.save();
    }

    //更新用户
    public static boolean update(User user){
        if (!StrKit.isBlank(user.getPassword())){
            String salt = getSalt();
            user.setSalt(salt);
            user.setPassword(getPassword(salt, user.getPassword()));
        }
        user.setUpdateTime(TimeUtil.getNow());
        return user.update();
    }

    public static boolean deleteByIds(int[] ids){
        return SqlBuilder.dao(User.dao).fakeDelete(ids);
    }

    public static boolean deleteRoleByIds(int[] ids){
        return SqlBuilder.dao(UserRole.dao).fakeDelete(ids);
    }

    //屏蔽用户
    public static boolean disableByIds(int[] ids, boolean isDisable){
        if(ids == null || ids.length == 0){
            return false;
        }else{
            String idsStr = ArrayStrUtil.join(ids);
            long now = TimeUtil.getNow();
            int result = Db.update("update tp_user set is_disable=?,update_time=? where id in ("+idsStr+")",isDisable,now);
            return result > 0;
        }
    }

    //用户组列表
    public static Page<UserRole> getGroupPage(FormFilter filter) {
        return SqlBuilder.dao(UserRole.dao)
                .columns("id", "name", "add_time", "is_common", "remark", "title")
                .where("is_delete", 0)
                .where("is_show", 1)
                .orderBy("id", "desc")
                .page(filter);
    }

}
