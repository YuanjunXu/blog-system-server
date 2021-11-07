package blog.system.server.service;

import blog.system.server.entity.UserEntity;
import blog.system.server.utils.ResponseResult;

public interface IUserService {

    ResponseResult register(UserEntity sobUser, String emailCode, String captchaCode);


    ResponseResult deleteUserById(String userId);

    ResponseResult listUsers(int page, int size, String userName, String email);

    ResponseResult updateUserPassword(String verifyCode, UserEntity sobUser);

    ResponseResult updateEmail(String email, String verifyCode);



    ResponseResult resetPassword(String userId, String password);


    UserEntity checkSobUser();
}
