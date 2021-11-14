package blog.system.server.services;


import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;

public interface IUserInfoService {
    ResponseResult getUserInfo(String userId);

    ResponseResult updateUserInfo(String userId, SobUser sobUser);
}
