package blog.system.server.service;

import blog.system.server.entity.UserEntity;
import blog.system.server.utils.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface IUserAccountService {
    ResponseResult initManagerAccount(UserEntity sobUser, HttpServletRequest request);

    ResponseResult doLogin(String captcha,
                           UserEntity sobUser, String from);

    ResponseResult doLogout();


    ResponseResult checkQrCodeLoginState(String loginId);

    ResponseResult updateQrCodeLoginState(String loginId);
}
