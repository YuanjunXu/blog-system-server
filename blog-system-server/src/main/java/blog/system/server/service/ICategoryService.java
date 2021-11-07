package blog.system.server.service;

import blog.system.server.entity.CategoriesEntity;
import blog.system.server.utils.ResponseResult;

import java.util.List;

public interface ICategoryService {


    ResponseResult addCategory(CategoriesEntity category);

    ResponseResult<CategoriesEntity> getCategory(String categoryId);

    ResponseResult<List<CategoriesEntity>> listCategories();

    ResponseResult updateCategory(String categoryId, CategoriesEntity category);

    ResponseResult deleteCategory(String categoryId);
    
}
