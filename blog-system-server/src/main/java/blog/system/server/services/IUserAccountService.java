package blog.system.server.services;


import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface IUserAccountService {
    ResponseResult initManagerAccount(SobUser sobUser, HttpServletRequest request);

    ResponseResult doLogin(String captcha,
                           SobUser sobUser, String from);

    ResponseResult doLogout();


    ResponseResult checkQrCodeLoginState(String loginId);

    ResponseResult updateQrCodeLoginState(String loginId);
}
