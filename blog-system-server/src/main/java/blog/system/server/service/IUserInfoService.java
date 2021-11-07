package blog.system.server.service;

import blog.system.server.entity.UserEntity;
import blog.system.server.utils.ResponseResult;

public interface IUserInfoService {
    ResponseResult<UserEntity> getUserInfo(String userId);

    ResponseResult updateUserInfo(String userId, UserEntity sobUser);
}
