package blog.system.server.controller.user;

import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/user_info")
public class UserInfoApi {

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 获取作者信息user-info
     *
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId") String userId) {
        return userInfoService.getUserInfo(userId);
    }

    /**
     * 修改用户信息user-info
     * <p>
     * 允许用户修改的内容
     * 1、头像
     * 2、用户名 （唯一的）
     * 2.5、签名
     * 3、密码 （单独修改）
     * 4、Email （唯一的，单独修改）
     *
     * @return
     */
    @PutMapping("/{userId}")
    public ResponseResult updateUserInfo(@PathVariable("userId") String userId, @RequestBody SobUser sobUser) {
        return userInfoService.updateUserInfo(userId, sobUser);
    }

}
