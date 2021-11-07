package blog.system.server.controller.admin;

import blog.system.server.entity.FriendsEntity;
import blog.system.server.interceptor.CheckTooFrequentCommit;
import blog.system.server.service.IFriendLinkService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "友情链接")
@RestController
@RequestMapping("/admin/friend_link")
public class FriendLinkAdminApi {

    @Autowired
    private IFriendLinkService friendLinkService;

    @ApiOperation("添加友链")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addFriendsLink(@RequestBody FriendsEntity friendLink) {
        return friendLinkService.addFriendLink(friendLink);
    }

    @ApiOperation("删除友链")
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{friendLinkId}")
    public ResponseResult deleteFriendLink(@PathVariable("friendLinkId") String friendLinkId) {
        return friendLinkService.deleteFriendLink(friendLinkId);
    }

    @ApiOperation("更新友链")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{friendLinkId}")
    public ResponseResult updateFriendLink(@PathVariable("friendLinkId") String friendLinkId,
                                           @RequestBody FriendsEntity friendLink) {
        return friendLinkService.updateFriendLink(friendLinkId, friendLink);
    }

    @ApiOperation("获取友链")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{friendLinkId}")
    public ResponseResult getFriendLink(@PathVariable("friendLinkId") String friendLinkId) {
        return friendLinkService.getFriendLink(friendLinkId);
    }

    @ApiOperation("友链列表")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listFriendLinks() {
        return friendLinkService.listFriendLinks();
    }
}
