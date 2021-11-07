package blog.system.server.controller.user;

import blog.system.server.entity.UserEntity;
import blog.system.server.service.IUserInfoService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户信息")
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
    @ApiOperation("获取作者信息")
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
    @ApiOperation("修改用户信息")
    @PutMapping("/{userId}")
    public ResponseResult updateUserInfo(@PathVariable("userId") String userId, @RequestBody UserEntity sobUser) {
        return userInfoService.updateUserInfo(userId, sobUser);
    }

}
