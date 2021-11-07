package blog.system.server.serviceImpl;


import blog.system.server.dao.LooperEntityRepository;
import blog.system.server.entity.LooperEntity;
import blog.system.server.entity.UserEntity;
import blog.system.server.service.ILoopService;
import blog.system.server.service.IUserService;
import blog.system.server.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private LooperEntityRepository loopDao;

    @Override
    public ResponseResult addLoop(LooperEntity looper) {
        //检查数据
        String title = looper.getTitle();
        if (TextUtils.isEmpty(title)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST,"标题不可以为空.");
        }
        String imageUrl = looper.getImageUrl();
        if (TextUtils.isEmpty(imageUrl)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST,"图片不可以为空.");
        }
        String targetUrl = looper.getTargetUrl();
        if (TextUtils.isEmpty(targetUrl)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST,"跳转链接不可以为空.");
        }
        //补充数据
        looper.setId(idWorker.nextId() + "");
        looper.setCreateTime(new Date());
        looper.setUpdateTime(new Date());
        //保存数据
        loopDao.save(looper);
        //返回结果
        return new ResponseResult(HttpStatus.OK,"轮播图添加成功.");
    }

    @Override
    public ResponseResult<LooperEntity> getLoop(String loopId) {
        LooperEntity loop = loopDao.findOneById(loopId);
        if (loop == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST,"轮播图不存在.");
        }
        return new ResponseResult(HttpStatus.OK,"轮播图获取成功.",loop);
    }

    @Autowired
    private IUserService userService;

    @Override
    public ResponseResult<List<LooperEntity>> listLoops() {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        UserEntity sobUser = userService.checkSobUser();
        List<LooperEntity> all;
        if (sobUser == null || !Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            //只能获取到正常的category
            all = loopDao.listLoopByState("1");
        } else {
            //查询
            all = loopDao.findAll(sort);
        }
        return new ResponseResult(HttpStatus.OK,"获取轮播图列表成功.",all);
    }

    @Override
    public ResponseResult updateLoop(String loopId, LooperEntity looper) {
        //找出来
        LooperEntity loopFromDb = loopDao.findOneById(loopId);
        if (loopFromDb == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST,"轮播图不存在.");
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
        return new ResponseResult(HttpStatus.OK,"轮播图更新成功.");
    }

    @Override
    public ResponseResult deleteLoop(String loopId) {
        loopDao.deleteById(loopId);
        return new ResponseResult(HttpStatus.OK,"删除成功.");
    }

}
