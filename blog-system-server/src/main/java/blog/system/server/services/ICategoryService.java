package blog.system.server.services;


import blog.system.server.pojo.Category;
import blog.system.server.response.ResponseResult;

public interface ICategoryService {


    ResponseResult addCategory(Category category);

    ResponseResult getCategory(String categoryId);

    ResponseResult listCategories();

    ResponseResult updateCategory(String categoryId, Category category);

    ResponseResult deleteCategory(String categoryId);
    
}
