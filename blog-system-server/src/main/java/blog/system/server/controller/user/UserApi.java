package blog.system.server.controller.user;


import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserApi {

    @Autowired
    private IUserService userService;

    /**
     * 注册
     *
     * @param sobUser
     * @return
     */
    @PostMapping("/join_in")
    public ResponseResult register(@RequestBody SobUser sobUser,
                                   @RequestParam("email_code") String emailCode,
                                   @RequestParam("captcha_code") String captchaCode) {

        return userService.register(sobUser, emailCode, captchaCode);
    }



    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listUsers(@RequestParam("page") int page,
                                    @RequestParam("size") int size,
                                    @RequestParam(value = "userName", required = false) String userName,
                                    @RequestParam(value = "email", required = false) String email) {
        return userService.listUsers(page, size, userName, email);
    }

    /**
     * 修改密码password
     * 修改密码
     * 普通做法：通过旧密码对比来更新密码
     * <p>
     * 即可以找回密码，也可以修改密码
     * 发送验证码到邮箱/手机---> 判断验证码是否真确来判断
     * 对应邮箱/手机号码所注册的账号是否属于你。
     * <p>
     * 步骤：
     * 1、用户填写邮箱
     * 2、用户获取验证码type=forget
     * 3、填写验证码
     * 4、填写新的密码
     * 5、提交数据
     * <p>
     * 数据包括：
     * <p>
     * 1、邮箱和新密码
     * 2、验证码
     * <p>
     * 如果验证码正确-->所用邮箱注册的账号就是你的，可以修改密码
     *
     * @return
     */
    @PutMapping("/password/{verifyCode}")
    public ResponseResult updatePassword(@PathVariable("verifyCode") String verifyCode,
                                         @RequestBody SobUser sobUser) {
        return userService.updateUserPassword(verifyCode, sobUser);
    }


    /**
     * 需要管理员权限
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(@PathVariable("userId") String userId) {
        //判断当前操作的用户是谁
        //根据用户角色判断是否可以删除
        //通过注解的方式来控制权限
        return userService.deleteUserById(userId);
    }


    /**
     * 1、必须已经登录了
     * 2、新的邮箱没有注册过
     * <p>
     * 用户的步骤：
     * 1、已经登录
     * 2、输入新的邮箱地址
     * 3、获取验证码 type=update
     * 4、输入验证码
     * 5、提交数据
     * <p>
     * 需要提交的数据
     * 1、新的邮箱地址
     * 2、验证码
     * 3、其他信息我们可以token里获取
     *
     * @return
     */
    @PutMapping("/email")
    public ResponseResult updateEmail(@RequestParam("email") String email,
                                      @RequestParam("verify_code") String verifyCode) {
        return userService.updateEmail(email, verifyCode);
    }



    @PreAuthorize("@permission.admin()")
    @PutMapping("/reset_password/{userId}")
    public ResponseResult resetPassword(@PathVariable("userId") String userId, @RequestParam("password") String password) {
        return userService.resetPassword(userId, password);
    }




}
