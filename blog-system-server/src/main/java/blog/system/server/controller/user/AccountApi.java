package blog.system.server.controller.user;

import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.IUserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user/account")
public class AccountApi {

    @Autowired
    private IUserAccountService userAccountService;

    /**
     * 初始化管理员账号-init-admin
     *
     * @return
     */
    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody SobUser sobUser, HttpServletRequest request) {
        log.info("user name == > " + sobUser.getUserName());
        log.info("password == > " + sobUser.getPassword());
        log.info("email == > " + sobUser.getEmail());
        return userAccountService.initManagerAccount(sobUser, request);
    }


    /**
     * 登录sign-up
     * <p>
     * 需要提交的数据
     * 1、用户账号-可以昵称，可以邮箱--->做了唯一处理
     * 2、密码
     * 3、图灵验证码
     * 4、图灵验证的key
     *
     * @param captcha 图灵验证码
     * @param sobUser 用户bean类，封装着账号和密码
     * @return
     */
    @PostMapping("/login/{captcha}/")
    public ResponseResult login(@PathVariable("captcha") String captcha,
                                @RequestBody SobUser sobUser,
                                @RequestParam(value = "from", required = false) String from) {
        return userAccountService.doLogin(captcha, sobUser, from);
    }


    /**
     * 退出登录
     * <p>
     * 拿到token_key
     * -> 删除redis里对应的token
     * -> 删除mysql里对应的refreshToken
     * -> 删除cookie里的token_key
     *
     * @return
     */
    @GetMapping("/logout")
    public ResponseResult logout() {
        return userAccountService.doLogout();
    }

    @PutMapping("/qr_code_state/{loginId}")
    public ResponseResult updateQrCodeLoginState(@PathVariable("loginId") String loginId) {
        return userAccountService.updateQrCodeLoginState(loginId);
    }

    /**
     * 检查二维码的登录状态
     *
     * @return
     */
    @GetMapping("/qr_code_state/{loginId}")
    public ResponseResult checkQrCodeLoginState(@PathVariable("loginId") String loginId) {
        return userAccountService.checkQrCodeLoginState(loginId);
    }


}
