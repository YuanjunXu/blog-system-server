package blog.system.server.services.impl;


import blog.system.server.dao.FriendLinkDao;
import blog.system.server.pojo.FriendLink;
import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.IFriendLinkService;
import blog.system.server.services.IUserService;
import blog.system.server.utils.Constants;
import blog.system.server.utils.IdWorker;
import blog.system.server.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    private FriendLinkDao friendLinkDao;

    /**
     * 添加友情连接
     *
     * @param friendLink
     * @return
     */
    @Override
    public ResponseResult addFriendLink(FriendLink friendLink) {
        //判断数据
        String url = friendLink.getUrl();
        if (TextUtils.isEmpty(url)) {
            return ResponseResult.FAILED("链接Url不可以为空.");
        }
        String logo = friendLink.getLogo();
        if (TextUtils.isEmpty(logo)) {
            return ResponseResult.FAILED("logo不可以为空.");
        }
        String name = friendLink.getName();
        if (TextUtils.isEmpty(name)) {
            return ResponseResult.FAILED("对方网站名不可以为空.");
        }
        //补全数据
        friendLink.setId(idWorker.nextId() + "");
        friendLink.setUpdateTime(new Date());
        friendLink.setCreateTime(new Date());
        //保存数据
        friendLinkDao.save(friendLink);
        //返回结果
        return ResponseResult.SUCCESS("添加成功.");
    }

    @Override
    public ResponseResult getFriendLink(String friendLinkId) {
        FriendLink friendLink = friendLinkDao.findOneById(friendLinkId);
        if (friendLink == null) {
            return ResponseResult.FAILED("友情链接不存");
        }
        return ResponseResult.SUCCESS("获取成功").setData(friendLink);
    }

    @Autowired
    private IUserService userService;

    @Override
    public ResponseResult listFriendLinks() {
        //创建条件
        Sort sort = new Sort(Sort.Direction.DESC, "createTime", "order");
        List<FriendLink> all;
        SobUser sobUser = userService.checkSobUser();
        if (sobUser == null || !Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            //只能获取到正常的category
            all = friendLinkDao.listFriendLinkByState("1");
        } else {
            //查询
            all = friendLinkDao.findAll(sort);
        }
        return ResponseResult.SUCCESS("获取列表成功.").setData(all);
    }

    @Override
    public ResponseResult deleteFriendLink(String friendLinkId) {
        int result = friendLinkDao.deleteAllById(friendLinkId);
        if (result == 0) {
            return ResponseResult.FAILED("删除失败.");
        }
        return ResponseResult.SUCCESS("删除成功.");
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
    public ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink) {
        FriendLink friendLinkFromDb = friendLinkDao.findOneById(friendLinkId);
        if (friendLinkFromDb == null) {
            return ResponseResult.FAILED("更新失败.");
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
        return ResponseResult.SUCCESS("更新成功.");
    }

}
