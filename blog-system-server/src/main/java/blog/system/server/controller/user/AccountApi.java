package blog.system.server.controller.user;

import blog.system.server.entity.UserEntity;
import blog.system.server.service.IUserAccountService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "账号相关")
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
    @ApiOperation("初始化管理员账户")
    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody UserEntity sobUser, HttpServletRequest request) {
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
    @ApiOperation("登录")
    @PostMapping("/login/{captcha}/")
    public ResponseResult login(@PathVariable("captcha") String captcha,
                                @RequestBody UserEntity sobUser,
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
    @ApiOperation("登出")
    @GetMapping("/logout")
    public ResponseResult logout() {
        return userAccountService.doLogout();
    }

    @ApiOperation("更新二维码状态")
    @PutMapping("/qr_code_state/{loginId}")
    public ResponseResult updateQrCodeLoginState(@PathVariable("loginId") String loginId) {
        return userAccountService.updateQrCodeLoginState(loginId);
    }

    /**
     * 检查二维码的登录状态
     *
     * @return
     */
    @ApiOperation("检查二维码的登录状态")
    @GetMapping("/qr_code_state/{loginId}")
    public ResponseResult checkQrCodeLoginState(@PathVariable("loginId") String loginId) {
        return userAccountService.checkQrCodeLoginState(loginId);
    }


}
