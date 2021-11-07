package blog.system.server.serviceImpl;

import blog.system.server.dao.SettingsEntityRepository;
import blog.system.server.entity.SettingsEntity;
import blog.system.server.entity.UserEntity;
import blog.system.server.service.IUserAccountService;
import blog.system.server.utils.Constants;
import blog.system.server.utils.CookieUtils;
import blog.system.server.utils.ResponseResult;
import blog.system.server.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
@Transactional
@Service
public class UserAccountServiceImpl extends BaseUserService implements IUserAccountService {


    @Autowired
    private SettingsEntityRepository settingsDao;

    @Override
    public ResponseResult initManagerAccount(UserEntity sobUser, HttpServletRequest request) {
        //检查是否有初始化
        SettingsEntity managerAccountState = settingsDao.findOneByKey(Constants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        if (managerAccountState != null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "管理员账号已经初始化了");
        }
        //检查数据
        if (TextUtils.isEmpty(sobUser.getUserName())) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "用户名不能为空");
        }
        if (TextUtils.isEmpty(sobUser.getPassword())) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "密码不能为空");
        }
        if (TextUtils.isEmpty(sobUser.getEmail())) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "邮箱不能为空");
        }
        //补充数据
        sobUser.setId(String.valueOf(idWorker.nextId()));
        sobUser.setRoles(Constants.User.ROLE_ADMIN);
        sobUser.setAvatar(Constants.User.DEFAULT_AVATAR);
        sobUser.setState(Constants.User.DEFAULT_STATE);
        String remoteAddr = request.getRemoteAddr();
        String localAddr = request.getLocalAddr();
        log.info("remoteAddr == > " + remoteAddr);
        log.info("localAddr == > " + localAddr);
        sobUser.setLoginIp(remoteAddr);
        sobUser.setRegIp(remoteAddr);
        sobUser.setCreateTime(new Date());
        sobUser.setUpdateTime(new Date());
        //对密码进行加密
        //原密码
        String password = sobUser.getPassword();
        //加密码
        String encode = bCryptPasswordEncoder.encode(password);
        sobUser.setPassword(encode);
        //保存到数据库里
        userDao.save(sobUser);
        //更新已经添加的标记
        //肯定滑的
        SettingsEntity setting = new SettingsEntity();
        setting.setId(idWorker.nextId() + "");
        setting.setKey(Constants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        setting.setCreateTime(new Date());
        setting.setUpdateTime(new Date());
        setting.setValue("1");
        settingsDao.save(setting);
        return new ResponseResult(HttpStatus.OK, "初始化成功");
    }


    @Override
    public ResponseResult doLogin(String captcha,
                                  UserEntity sobUser, String from) {
        //from可能不有值
        //如果不有值，就给它一个默认值
        if (TextUtils.isEmpty(from)
                || (!Constants.FROM_MOTILE.equals(from) && !Constants.FROM_PC.equals(from))) {
            from = Constants.FROM_MOTILE;
        }
        String captchaKey = CookieUtils.getCookie(getRequest(), Constants.User.LAST_CAPTCHA_ID);
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();
        String captchaValue = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (!captcha.equals(captchaValue)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "人类验证码不正确");
        }
        //验证成功，删除redis里的验证码
        redisUtils.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        //有可能是邮箱，也有可能是用户名
        String userName = sobUser.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "账号不可以为空.");
        }

        String password = sobUser.getPassword();
        if (TextUtils.isEmpty(password)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "密码不可以为空.");
        }

        UserEntity userFromDb = userDao.findOneByUserName(userName);
        if (userFromDb == null) {
            userFromDb = userDao.findOneByEmail(userName);
        }

        if (userFromDb == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "用户名或密码不正确");
        }
        //用户存在
        //对比密码
        boolean matches = bCryptPasswordEncoder.matches(password, userFromDb.getPassword());
        if (!matches) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "用户名或密码不正确");
        }
        //密码是正确
        //判断用户状态，如果是非正常的状态，则返回结果
        if (!"1".equals(userFromDb.getState())) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "账户已被禁用");
        }
        //修改更新时间和登录IP
        userFromDb.setLoginIp(request.getRemoteAddr());
        userFromDb.setUpdateTime(new Date());
        createToken(response, userFromDb, from);
        CookieUtils.deleteCookie(getResponse(), Constants.User.LAST_CAPTCHA_ID);
        return new ResponseResult(HttpStatus.OK, "登录成功");
    }


    /**
     * 检查二维码的登录状态
     * 结果有：
     * 1、登录成功（loginId对应的值为有ID内容）
     * 2、等待扫描（loginId对应的值为false）
     * 3、二维码已经过期了 loginId对应的值为null
     * <p>
     * 是被PC端轮询调用的
     *
     * @param loginId
     * @return
     */
    @Override
    public ResponseResult checkQrCodeLoginState(String loginId) {
        //从redis里取值出来
        ResponseResult result = checkLoginIdState(loginId);
        if (result != null) {
            return result;
        }
        //先等待一段时间，再去检查
        //如果超出了这个时间，我就们就返回等待扫码
        Callable<ResponseResult> callable = new Callable<ResponseResult>() {
            @Override
            public ResponseResult call() throws Exception {
                log.info("start waiting for scan...");
                //先阻塞
                countDownLatchManager.getLatch(loginId).await(Constants.User.QR_CODE_STATE_CHECK_WAITING_TIME,
                        TimeUnit.SECONDS);
                //收到状态更新的通知，我们就检查loginId对应的状态
                log.info("start check login state...");
                ResponseResult checkResult = checkLoginIdState(loginId);
                if (checkResult != null) {
                    return checkResult;
                }
                //超时则返回等待扫描
                //完事后，删除对应的latch
                countDownLatchManager.deleteLatch(loginId);
                return new ResponseResult(HttpStatus.BAD_REQUEST, "等待扫描");
            }
        };
        try {
            return callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseResult(HttpStatus.BAD_REQUEST, "等待扫描");
    }


    private ResponseResult checkLoginIdState(String loginId) {
        String loginState = (String) redisUtils.get(Constants.User.KEY_PC_LOGIN_ID + loginId);
        if (loginState == null) {
            //二维码过期
            return new ResponseResult(HttpStatus.BAD_REQUEST, "二维码已过期");
        }

        //不为false,且不为null，那么就是用户的ID了，也就是登录成功了
        if (!TextUtils.isEmpty(loginState) && !Constants.User.KEY_PC_LOGIN_STATE_FALSE.equals(loginState)) {
            //创建token，也就是走PC端的登录
            UserEntity userFromDb = userDao.findOneById(loginState);
            if (userFromDb == null) {
                return new ResponseResult(HttpStatus.BAD_REQUEST,"二维码已过期");
            }
            createToken(getResponse(), userFromDb, Constants.FROM_PC);
            CookieUtils.deleteCookie(getResponse(), Constants.User.LAST_REQUEST_LOGIN_ID);
            //登录成功
            return new ResponseResult(HttpStatus.OK,"登录成功");
        }
        return null;
    }


    @Override
    public ResponseResult doLogout() {
        //拿到token_key
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
        if (TextUtils.isEmpty(tokenKey)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST,"用户未登录");
        }
        //刪除redis里的token,因为各端是独立的，所以可以删除
        redisUtils.del(Constants.User.KEY_TOKEN + tokenKey);
        //删除mysql里的refreshToken
        //这个不做删除，只做更新
        //refreshTokenDao.deleteAllByTokenKey(tokenKey);
        if (Constants.FROM_PC.startsWith(tokenKey)) {
            refreshTokenDao.deletePcTokenKey(tokenKey);
        } else {
            refreshTokenDao.deleteMobileTokenKey(tokenKey);
        }
        //删除cookie里的token_key
        CookieUtils.deleteCookie(getResponse(), Constants.User.COOKIE_TOKE_KEY);
        return  new ResponseResult(HttpStatus.OK,"退出登录成功.");
    }


    /**
     * 更新二维码的登录状态
     *
     * @param loginId
     * @return
     */
    @Override
    public ResponseResult updateQrCodeLoginState(String loginId) {
        //1、检查用户是否登录
        UserEntity sobUser = checkSobUser();
        if (sobUser == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST,"用户未登录");
        }
        //2、改变loginId对应的值=true
        redisUtils.set(Constants.User.KEY_PC_LOGIN_ID + loginId, sobUser.getId());
        //2.1、通知正在等待的扫描任务
        countDownLatchManager.onPhoneDoLogin(loginId);
        //3、返回结果
        return  new ResponseResult(HttpStatus.OK,"退出登录成功.");
    }
}
