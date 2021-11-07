package blog.system.server.serviceImpl;

import blog.system.server.dao.FriendsEntityRepository;
import blog.system.server.entity.FriendsEntity;
import blog.system.server.entity.UserEntity;
import blog.system.server.service.IFriendLinkService;
import blog.system.server.service.IUserService;
import blog.system.server.utils.Constants;
import blog.system.server.utils.IdWorker;
import blog.system.server.utils.ResponseResult;
import blog.system.server.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FriendLinkServiceImpl extends BaseService implements IFriendLinkService {
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private FriendsEntityRepository friendLinkDao;

    /**
     * 添加友情连接
     *
     * @param friendLink
     * @return
     */
    @Override
    public ResponseResult addFriendLink(FriendsEntity friendLink) {
        //判断数据
        String url = friendLink.getUrl();
        if (TextUtils.isEmpty(url)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "链接Url不可以为空.");
        }
        String logo = friendLink.getLogo();
        if (TextUtils.isEmpty(logo)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "logo不可以为空.");
        }
        String name = friendLink.getName();
        if (TextUtils.isEmpty(name)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "对方网站名不可以为空.");
        }
        //补全数据
        friendLink.setId(idWorker.nextId() + "");
        friendLink.setUpdateTime(new Date());
        friendLink.setCreateTime(new Date());
        //保存数据
        friendLinkDao.save(friendLink);
        //返回结果
        return new ResponseResult(HttpStatus.OK, "添加成功.");
    }

    @Override
    public ResponseResult<FriendsEntity> getFriendLink(String friendLinkId) {
        FriendsEntity friendLink = friendLinkDao.findOneById(friendLinkId);
        if (friendLink == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "友情链接不存在");
        }
        return new ResponseResult(HttpStatus.OK, "获取成功", friendLink);
    }

    @Autowired
    private IUserService userService;

    @Override
    public ResponseResult<List<FriendsEntity>> listFriendLinks() {
        //创建条件
        Sort sort = new Sort(Sort.Direction.DESC, "createTime", "order");
        List<FriendsEntity> all;
        UserEntity sobUser = userService.checkSobUser();
        if (sobUser == null || !Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            //只能获取到正常的category
            all = friendLinkDao.listFriendLinkByState("1");
        } else {
            //查询
            all = friendLinkDao.findAll(sort);
        }

        return new ResponseResult(HttpStatus.OK,"获取列表成功.",all);
    }

    @Override
    public ResponseResult deleteFriendLink(String friendLinkId) {
        int result = friendLinkDao.deleteAllById(friendLinkId);
        if (result == 0) {
            return new ResponseResult(HttpStatus.BAD_REQUEST,"删除失败.");
        }
        return new ResponseResult(HttpStatus.OK,"删除成功.");
    }

    /**
     * 更新内容有什么：
     * logo
     * 对方网站的名称
     * url
     * order
     *
     * @param friendLinkId
     * @param friendLink
     * @return
     */
    @Override
    public ResponseResult updateFriendLink(String friendLinkId, FriendsEntity friendLink) {
        FriendsEntity friendLinkFromDb = friendLinkDao.findOneById(friendLinkId);
        if (friendLinkFromDb == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST,"更新失败.");
        }
        String logo = friendLink.getLogo();
        if (!TextUtils.isEmpty(logo)) {
            friendLinkFromDb.setLogo(logo);
        }
        String name = friendLink.getName();
        if (!TextUtils.isEmpty(name)) {
            friendLinkFromDb.setName(name);
        }
        String url = friendLink.getUrl();
        if (!TextUtils.isEmpty(url)) {
            friendLinkFromDb.setUrl(url);
        }
        if (!TextUtils.isEmpty(friendLink.getState())) {
            friendLinkFromDb.setState(friendLink.getState());
        }
        friendLinkFromDb.setOrder(friendLink.getOrder());
        friendLinkFromDb.setUpdateTime(new Date());
        //保存数据
        friendLinkDao.save(friendLinkFromDb);
        return new ResponseResult(HttpStatus.OK,"更新成功.");
    }

}
