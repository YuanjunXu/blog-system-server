package blog.system.server.serviceImpl;

import blog.system.server.dao.CategoriesEntityRepository;
import blog.system.server.entity.CategoriesEntity;
import blog.system.server.entity.UserEntity;
import blog.system.server.service.ICategoryService;
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
public class CategoryServiceImpl extends BaseService implements ICategoryService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CategoriesEntityRepository categoryDao;

    @Autowired
    private IUserService userService;


    @Override
    public ResponseResult addCategory(CategoriesEntity category) {
        //先检查数据
        // 必须的数据有：
        //分类名称、分类的pinyin、顺序、描述
        if (TextUtils.isEmpty(category.getName())) {
            return  new  ResponseResult(HttpStatus.BAD_REQUEST,"分类名称不可以为空.");
        }
        if (TextUtils.isEmpty(category.getPinyin())) {
            return  new  ResponseResult(HttpStatus.BAD_REQUEST,"分类拼音不可以为空.");
        }
        if (TextUtils.isEmpty(category.getDescription())) {
            return  new  ResponseResult(HttpStatus.BAD_REQUEST,"分类描述不可以为空.");
        }
        //补全数据
        category.setId(idWorker.nextId() + "");
        category.setStatus("1");
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        //保存数据
        categoryDao.save(category);
        //返回结果
        return  new  ResponseResult(HttpStatus.OK,"添加分类成功");
    }

    @Override
    public ResponseResult<CategoriesEntity> getCategory(String categoryId) {
        CategoriesEntity category = categoryDao.findOneById(categoryId);
        if (category == null) {
            return  new  ResponseResult(HttpStatus.BAD_REQUEST,"分类不存在.");
        }
        return  new  ResponseResult(HttpStatus.OK,"获取分类成功.",category);
    }

    @Override
    public ResponseResult<List<CategoriesEntity>> listCategories() {
        //参数检查
        //创建条件
        Sort sort = new Sort(Sort.Direction.DESC, "createTime", "order");
        //判断用户角色，普通 用户/未登录用户，只能获取到正常的category
        //管理员帐户，可以拿到所有的分类.
        UserEntity sobUser = userService.checkSobUser();
        List<CategoriesEntity> categories;
        if (sobUser == null || !Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            //只能获取到正常的category
            categories = categoryDao.listCategoriesByState("1");
        } else {
            //查询
            categories = categoryDao.findAll(sort);
        }
        //返回结果
        return new  ResponseResult(HttpStatus.OK,"获取分类列表成功.",categories);
    }

    @Override
    public ResponseResult updateCategory(String categoryId, CategoriesEntity category) {
        //第一步是找出来
        CategoriesEntity categoryFromDb = categoryDao.findOneById(categoryId);
        if (categoryFromDb == null) {
            return new  ResponseResult(HttpStatus.BAD_REQUEST,"分类不存在.");
        }
        //第二步是对内容判断，有些字段是不可以为空的
        String name = category.getName();
        if (!TextUtils.isEmpty(name)) {
            categoryFromDb.setName(name);
        }
        String pinyin = category.getPinyin();
        if (!TextUtils.isEmpty(pinyin)) {
            categoryFromDb.setPinyin(pinyin);
        }

        String description = category.getDescription();
        if (!TextUtils.isEmpty(description)) {
            categoryFromDb.setDescription(description);
        }
        categoryFromDb.setStatus(category.getStatus());
        categoryFromDb.setOrder(category.getOrder());
        categoryFromDb.setUpdateTime(new Date());
        //第三步是保存数据
        categoryDao.save(categoryFromDb);
        //返回结果
        return new  ResponseResult(HttpStatus.OK,"分类更新成功.");
    }

    @Override
    public ResponseResult deleteCategory(String categoryId) {
        int result = categoryDao.deleteCategoryByUpdateState(categoryId);
        if (result == 0) {
            return new  ResponseResult(HttpStatus.BAD_REQUEST,"该分类不存在.");
        }
        return new  ResponseResult(HttpStatus.OK,"删除分类成功.");
    }

}
