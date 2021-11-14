package blog.system.server.services.impl;

import blog.system.server.dao.UserDao;
import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.IUserInfoService;
import blog.system.server.utils.Constants;
import blog.system.server.utils.CookieUtils;
import blog.system.server.utils.TextUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class UserInfoServiceImpl extends BaseUserService implements IUserInfoService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private Gson gson;


    @Override
    public ResponseResult getUserInfo(String userId) {
        //从数据里获取
        SobUser user = userDao.findOneById(userId);
        //判断结果
        if (user == null) {
            //如果不存在，就返回不存在
            return ResponseResult.FAILED("用户不存在");
        }
        //如果存在，就复制对象，清空密码、Email、登录ID，注册IP
        String userJson = gson.toJson(user);
        SobUser newSobUser = gson.fromJson(userJson, SobUser.class);
        newSobUser.setPassword("");
        newSobUser.setEmail("");
        newSobUser.setRegIp("");
        newSobUser.setLoginIp("");
        //返回结果
        return ResponseResult.SUCCESS("获取成功.").setData(newSobUser);
    }

    /**
     * 更新用户信息
     *
     * @param userId
     * @param sobUser
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(String userId, SobUser sobUser) {
        //从token里解析出来的user，为了校验权限
        //只有用户才可以修改自己的信息
        SobUser userFromTokenKey = checkSobUser();
        if (userFromTokenKey == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        SobUser userFromDb = userDao.findOneById(userFromTokenKey.getId());

        //判断当前用户的ID和即将要修改的用户ID是否一致，如果一致才可以修改
        if (!userFromDb.getId().equals(userId)) {
            return ResponseResult.PERMISSION_DENIED();
        }
        //可以进行修改
        //可经修改的项
        //用户名
        String userName = sobUser.getUserName();
        if (!TextUtils.isEmpty(userName) && !userName.equals(userFromTokenKey.getUserName())) {
            SobUser userByUserName = userDao.findOneByUserName(userName);
            if (userByUserName != null) {
                return ResponseResult.FAILED("该用户已注册.");
            }
            userFromDb.setUserName(userName);
        }
        //头像
        if (!TextUtils.isEmpty(sobUser.getAvatar())) {
            userFromDb.setAvatar(sobUser.getAvatar());
        }
        userFromDb.setUpdateTime(new Date());
        //签名,可以为空
        userFromDb.setSign(sobUser.getSign());
        userDao.save(userFromDb);

        //干掉redis里的token，下一次请求，需要解析token的，就会根据refreshToken重新创建一个。
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
        redisUtils.del(Constants.User.KEY_TOKEN + tokenKey);
        return ResponseResult.SUCCESS("用户信息更新成功.");
    }

}
