package blog.system.server.controller.user;

import blog.system.server.response.ResponseResult;
import blog.system.server.services.IUserUtilsService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@RequestMapping("/user/utils")
public class UserUtilsApi {

    @Autowired
    private IUserUtilsService userUtilsService;

    @GetMapping("/check_email_code")
    public ResponseResult checkEmailCode(@RequestParam("email") String email,
                                         @RequestParam("emailCode") String emailCode,
                                         @RequestParam("captchaCode") String captchaCode) {
        return userUtilsService.checkEmailCode(email, emailCode, captchaCode);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/register_count")
    public ResponseResult getRegisterCount() {
        return userUtilsService.getRegisterCount();
    }

    @GetMapping("/check_token")
    public ResponseResult parseToken() {
        return userUtilsService.parseToken();
    }


    /**
     * 检查该Email是否已经注册
     *
     * @param userName 用户名
     * @return SUCCESS -- > 已经注册了，FAILED ===> 没有注册
     */
    @ApiResponses({
            @ApiResponse(code = 20000, message = "表示用户名已经注册了"),
            @ApiResponse(code = 40000, message = "表示用户名未注册")
    })
    @GetMapping("/user_name")
    public ResponseResult checkUserName(@RequestParam("userName") String userName) {
        return userUtilsService.checkUserName(userName);
    }

    /***
     * 获取二维码：
     * 二维码的图片路径
     * 二维码的内容字符串
     * @return
     */
    @GetMapping("/pc_login_qr_code")
    public ResponseResult getPcLoginQrCode() {
        return userUtilsService.getPcLoginQrCodeInfo();
    }

    /**
     * 检查该Email是否已经注册
     *
     * @param email 邮箱地址
     * @return SUCCESS -- > 已经注册了，FAILED ===> 没有注册
     */
    @ApiResponses({
            @ApiResponse(code = 20000, message = "表示当前邮箱已经注册了"),
            @ApiResponse(code = 40000, message = "表示当前邮箱未注册")
    })
    @GetMapping("/email")
    public ResponseResult checkEmail(@RequestParam("email") String email) {
        return userUtilsService.checkEmail(email);
    }

    /**
     * 发送邮件email
     * <p>
     * 使用场景：注册、找回密码、修改邮箱（会输入新的邮箱）
     * 注册：如果已经注册过了，就提示说，该邮箱已经注册
     * 找回密码：如果没有注册过，提示该邮箱没有注册
     * 修改邮箱（新的邮箱）：如果已经注册了，提示改邮箱已经注册
     * <p>
     * 注册(register)：如果已经注册过了，就提示说，该邮箱已经注册
     * 找回密码(forget)：如果没有注册过，提示该邮箱没有注册
     * 修改邮箱(update)（新的邮箱）：如果已经注册了，提示改邮箱已经注册
     *
     * @return
     */
    @GetMapping("/verify_code")
    public ResponseResult sendVerifyCode(HttpServletRequest request, @RequestParam("type") String type,
                                         @RequestParam("email") String emailAddress,
                                         @RequestParam("captchaCode") String captchaCode) {
        log.info("email == > " + emailAddress);
        return userUtilsService.sendEmail(type, request, emailAddress, captchaCode);
    }

    /**
     * 获取图灵验证码
     * 有效时长为10分钟
     *
     * @return
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response) {
        try {
            userUtilsService.createCaptcha(response);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }


}
