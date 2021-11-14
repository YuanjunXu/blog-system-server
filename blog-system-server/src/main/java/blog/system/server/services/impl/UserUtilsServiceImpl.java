package blog.system.server.services.impl;

import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.IUserUtilsService;
import blog.system.server.utils.Constants;
import blog.system.server.utils.CookieUtils;
import blog.system.server.utils.TextUtils;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Transactional
@Slf4j
public class UserUtilsServiceImpl extends BaseUserService implements IUserUtilsService {


    @Autowired
    private TaskService taskService;


    @Autowired
    private Random random;


    public static final int[] captcha_font_types = {Captcha.FONT_1
            , Captcha.FONT_2
            , Captcha.FONT_3
            , Captcha.FONT_4
            , Captcha.FONT_5
            , Captcha.FONT_6
            , Captcha.FONT_7
            , Captcha.FONT_8
            , Captcha.FONT_9
            , Captcha.FONT_10};



    @Override
    public ResponseResult getRegisterCount() {
        long count = userDao.count();
        return ResponseResult.SUCCESS("获取用户总数成功.").setData(count);
    }

    @Override
    public ResponseResult checkEmailCode(String email, String emailCode, String captchaCode) {
        //检查人类验证
        String captchaId = CookieUtils.getCookie(getRequest(), Constants.User.LAST_CAPTCHA_ID);
        String captcha = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaId);
        if (!captchaCode.equals(captcha)) {
            return ResponseResult.FAILED("人类验证码不正确");
        }
        //检查邮箱code
        String redisVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (!emailCode.equals(redisVerifyCode)) {
            return ResponseResult.FAILED("邮箱验证码不正确");
        }
        return ResponseResult.SUCCESS("验证码正确");
    }


    @Override
    public ResponseResult getPcLoginQrCodeInfo() {
        //尝试取出上一次的loginId
        String lastLoginId = CookieUtils.getCookie(getRequest(), Constants.User.LAST_REQUEST_LOGIN_ID);
//        if (!TextUtils.isEmpty(lastLoginId)) {
//            //先把redis里的删除
//            redisUtils.del(Constants.User.KEY_PC_LOGIN_ID + lastLoginId);
//            Object lastGetTime = redisUtils.get(Constants.User.LAST_REQUEST_LOGIN_ID + lastLoginId);
//            if (lastGetTime != null) {
//                return ResponseResult.FAILED("服务器繁忙，请稍后重试.");
//            }
//        }
        // 1、生成一个唯一的ID
        long code;
        if (!TextUtils.isEmpty(lastLoginId)) {
            code = Long.parseLong(lastLoginId);
        } else {
            code = idWorker.nextId();
        }
        // 2、保存到redis里，值为false，时间为5分钟（二维码的有效期）
        redisUtils.set(Constants.User.KEY_PC_LOGIN_ID + code,
                Constants.User.KEY_PC_LOGIN_STATE_FALSE,
                Constants.TimeValueInSecond.MIN_5);
        Map<String, Object> result = new HashMap<>();
        String originalDomain = TextUtils.getDomain(getRequest());
        result.put("code", String.valueOf(code));
        result.put("url", originalDomain + "/portal/image/qr-code/" + code);
        CookieUtils.setUpCookie(getResponse(), Constants.User.LAST_REQUEST_LOGIN_ID, String.valueOf(code));
        //redisUtils.set(Constants.User.LAST_REQUEST_LOGIN_ID + String.valueOf(code), "true",
        //     Constants.TimeValueInSecond.SECOND_10);
        // 返回结果
        return ResponseResult.SUCCESS("获取成功.").setData(result);
    }



    @Override
    public ResponseResult parseToken() {
        SobUser sobUser = checkSobUser();
        if (sobUser == null) {
            return ResponseResult.FAILED("用户未登录.");
        }
        return ResponseResult.SUCCESS("获取用户成功.").setData(sobUser);
    }


    @Override
    public ResponseResult checkEmail(String email) {
        SobUser user = userDao.findOneByEmail(email);
        return user == null ? ResponseResult.FAILED("该邮箱未注册.") : ResponseResult.SUCCESS("该邮箱已经注册.");
    }

    @Override
    public ResponseResult checkUserName(String userName) {
        SobUser user = userDao.findOneByUserName(userName);
        return user == null ? ResponseResult.FAILED("该用户名未注册.") : ResponseResult.SUCCESS("该用户名已经存在.");
    }

    @Override
    public void createCaptcha(HttpServletResponse response) throws Exception {
        //放置重新创建，占用redis里太多资源
        //先检查上一次的id，如果有话重复利用
        String lastId = CookieUtils.getCookie(getRequest(), Constants.User.LAST_CAPTCHA_ID);
        String key;
        if (TextUtils.isEmpty(lastId)) {
            key = idWorker.nextId() + "";
        } else {
            key = lastId;
        }
        //可以用了
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        int captchaType = random.nextInt(3);
        Captcha targetCaptcha;
        int width = 120;
        int height = 40;

        if (captchaType == 0) {
            // 三个参数分别为宽、高、位数
            targetCaptcha = new SpecCaptcha(width, height, 5);
        } else if (captchaType == 1) {
            // gif类型
            targetCaptcha = new GifCaptcha(width, height);
        } else {
            // 算术类型
            targetCaptcha = new ArithmeticCaptcha(width, height);
            targetCaptcha.setLen(2);  // 几位数运算，默认是两位
        }
        int index = random.nextInt(captcha_font_types.length);
        log.info("captcha font type index == > " + index);
        targetCaptcha.setFont(captcha_font_types[index]);
        targetCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        String content = targetCaptcha.text().toLowerCase();
        log.info("captcha content == > " + content);
        //保存到redis里头
        //删除时机
        //1、自然过期，也就是10分钟后自己删除
        //2、验证码用完以后删除
        //3、用完的情况：看get的地方
        //把这个id写到cookie里，后提交的时候，用于查询验证的正确性
        CookieUtils.setUpCookie(response, Constants.User.LAST_CAPTCHA_ID, key);
        redisUtils.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 10);
        targetCaptcha.out(response.getOutputStream());
    }



    /**
     * 发送邮箱验证码
     * 使用场景：注册、找回密码、修改邮箱（会输入新的邮箱）
     * 注册(register)：如果已经注册过了，就提示说，该邮箱已经注册
     * 找回密码(forget)：如果没有注册过，提示该邮箱没有注册
     * 修改邮箱(update)（新的邮箱）：如果已经注册了，提示改邮箱已经注册
     *
     * @param request
     * @param address
     * @param address
     * @return
     */
    @Override
    public ResponseResult sendEmail(String type, HttpServletRequest request, String address, String captchaCode) {
        //检查人类验证码是否正确
        //从cookies里拿到key
        String captchaId = CookieUtils.getCookie(request, Constants.User.LAST_CAPTCHA_ID);
        String captchaValue = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaId);
        if (!captchaCode.equals(captchaValue)) {
            return ResponseResult.FAILED("人类验证码不正确");
        }
        if (address == null) {
            return ResponseResult.FAILED("邮箱地址不可以为空");
        }
        //根据类型，查询邮箱是否存在
        if ("register".equals(type) || "update".equals(type)) {
            SobUser userByEmail = userDao.findOneByEmail(address);
            if (userByEmail != null) {
                return ResponseResult.FAILED("该邮箱已注册");
            }
        } else if ("forget".equals(type)) {
            SobUser userByEmail = userDao.findOneByEmail(address);
            if (userByEmail == null) {
                return ResponseResult.FAILED("该邮箱未注册");
            }
        }
        //1、防止暴力发送，就是不断发地送：同一个邮箱，间隔要超过30秒发一次，同一个Ip，1小时内最多只能发10次（如果是短信，你最多只能发5次）
        String remoteAddr = request.getRemoteAddr();
        log.info("sendEmail == > ip == > " + remoteAddr);
        if (remoteAddr != null) {
            remoteAddr = remoteAddr.replaceAll(":", "_");
        }
        //拿出来，如果没有，那就过了
        log.info("Constants.User.KEY_EMAIL_SEND_IP + remoteAddr === > " + Constants.User.KEY_EMAIL_SEND_IP + remoteAddr);
        String ipSendTimeValue = (String) redisUtils.get(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr);
        Integer ipSendTime;
        if (ipSendTimeValue != null) {
            ipSendTime = Integer.parseInt(ipSendTimeValue);
        } else {
            ipSendTime = 1;
        }
        if (ipSendTime > 10) {
            return ResponseResult.FAILED("您发送验证码也太频繁了吧！");
        }
        Object hasEmailSend = redisUtils.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + address);
        if (hasEmailSend != null) {
            return ResponseResult.FAILED("您发送验证码也太频繁了吧！");
        }
        //2、检查邮箱地址是否正确
        boolean isEmailFormatOk = TextUtils.isEmailAddressOk(address);
        if (!isEmailFormatOk) {
            return ResponseResult.FAILED("邮箱地址格式不正确");
        }
        // 0~999999
        int code = random.nextInt(999999);
        if (code < 100000) {
            code += 100000;
        }
        log.info("sendEmail ==> code == > " + code);
        //3、发送验证码,6位数：100000~999999
        try {
            taskService.sendEmailVerifyCode(String.valueOf(code), address);
        } catch (Exception e) {
            return ResponseResult.FAILED("验证码发送失败，请稍后重试.");
        }
        //4、做记录
        //发送记录，code
        //
        if (ipSendTime == null) {
            ipSendTime = 0;
        }
        ipSendTime++;
        //1个小时有效期
        redisUtils.set(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr, String.valueOf(ipSendTime), 60 * 60);
        redisUtils.set(Constants.User.KEY_EMAIL_SEND_ADDRESS + address, "true", 30);
        //保存code，10分钟内有效
        redisUtils.set(Constants.User.KEY_EMAIL_CODE_CONTENT + address, String.valueOf(code), 60 * 10);
        return ResponseResult.SUCCESS("验证码发送成功");
    }
}
