package blog.system.server.services;


import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;

public interface IUserService {

    ResponseResult register(SobUser sobUser, String emailCode, String captchaCode);


    ResponseResult deleteUserById(String userId);

    ResponseResult listUsers(int page, int size, String userName, String email);

    ResponseResult updateUserPassword(String verifyCode, SobUser sobUser);

    ResponseResult updateEmail(String email, String verifyCode);



    ResponseResult resetPassword(String userId, String password);


    SobUser checkSobUser();
}
