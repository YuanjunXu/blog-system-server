package blog.system.server.service;

import blog.system.server.entity.FriendsEntity;
import blog.system.server.utils.ResponseResult;

import java.util.List;

public interface IFriendLinkService {
    ResponseResult addFriendLink(FriendsEntity friendLink);

    ResponseResult<FriendsEntity> getFriendLink(String friendLinkId);

    ResponseResult<List<FriendsEntity>> listFriendLinks();

    ResponseResult deleteFriendLink(String friendLinkId);

    ResponseResult updateFriendLink(String friendLinkId, FriendsEntity friendLink);
    
}
