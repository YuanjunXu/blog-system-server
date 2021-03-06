package blog.system.server.services.impl;


import blog.system.server.dao.LoopDao;
import blog.system.server.pojo.Looper;
import blog.system.server.pojo.SobUser;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.ILoopService;
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
public class LoopServiceImpl extends BaseService implements ILoopService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private LoopDao loopDao;

    @Override
    public ResponseResult addLoop(Looper looper) {
        //检查数据
        String title = looper.getTitle();
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("标题不可以为空.");
        }
        String imageUrl = looper.getImageUrl();
        if (TextUtils.isEmpty(imageUrl)) {
            return ResponseResult.FAILED("图片不可以为空.");
        }
        String targetUrl = looper.getTargetUrl();
        if (TextUtils.isEmpty(targetUrl)) {
            return ResponseResult.FAILED("跳转链接不可以为空.");
        }
        //补充数据
        looper.setId(idWorker.nextId() + "");
        looper.setCreateTime(new Date());
        looper.setUpdateTime(new Date());
        //保存数据
        loopDao.save(looper);
        //返回结果
        return ResponseResult.SUCCESS("轮播图添加成功.");
    }

    @Override
    public ResponseResult getLoop(String loopId) {
        Looper loop = loopDao.findOneById(loopId);
        if (loop == null) {
            return ResponseResult.FAILED("轮播图不存在.");
        }
        return ResponseResult.SUCCESS("轮播图获取成功.").setData(loop);
    }

    @Autowired
    private IUserService userService;

    @Override
    public ResponseResult listLoops() {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        SobUser sobUser = userService.checkSobUser();
        List<Looper> all;
        if (sobUser == null || !Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            //只能获取到正常的category
            all = loopDao.listLoopByState("1");
        } else {
            //查询
            all = loopDao.findAll(sort);
        }
        return ResponseResult.SUCCESS("获取轮播图列表成功.").setData(all);
    }

    @Override
    public ResponseResult updateLoop(String loopId, Looper looper) {
        //找出来
        Looper loopFromDb = loopDao.findOneById(loopId);
        if (loopFromDb == null) {
            return ResponseResult.FAILED("轮播图不存在.");
        }
        //不可以为空的，要判空
        String title = looper.getTitle();
        if (!TextUtils.isEmpty(title)) {
            loopFromDb.setTitle(title);
        }
        String targetUrl = looper.getTargetUrl();
        if (!TextUtils.isEmpty(targetUrl)) {
            loopFromDb.setTargetUrl(targetUrl);
        }
        String imageUrl = looper.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            loopFromDb.setImageUrl(imageUrl);
        }
        if (!TextUtils.isEmpty(looper.getState())) {
            loopFromDb.setState(looper.getState());
        }
        loopFromDb.setOrder(looper.getOrder());
        loopFromDb.setUpdateTime(new Date());
        //可以为空的直接设置
        //保存回去
        loopDao.save(loopFromDb);
        return ResponseResult.SUCCESS("轮播图更新成功.");
    }

    @Override
    public ResponseResult deleteLoop(String loopId) {
        loopDao.deleteById(loopId);
        return ResponseResult.SUCCESS("删除成功.");
    }

}
