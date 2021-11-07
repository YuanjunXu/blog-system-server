package blog.system.server.controller.admin;

import blog.system.server.entity.CategoriesEntity;
import blog.system.server.interceptor.CheckTooFrequentCommit;
import blog.system.server.service.ICategoryService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 管理中心，分类的API
 */
@Api(tags = "分类管理")
@RestController
@RequestMapping("/admin/category")
public class CategoryAdminApi {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 添加分类
     * 需要管理员权限
     *
     * @return
     */
    @ApiOperation("添加分类")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoriesEntity category) {
        return categoryService.addCategory(category);
    }

    /**
     * 删除分类
     *
     * @param categoryId
     * @return
     */
    @ApiOperation("删除分类")
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{categoryId}")
    public ResponseResult deleteCategory(@PathVariable("categoryId") String categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    /**
     * 更新分类
     *
     * @param categoryId
     * @param category
     * @return
     */
    @ApiOperation("更新分类")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{categoryId}")
    public ResponseResult updateCategory(@PathVariable("categoryId") String categoryId, @RequestBody CategoriesEntity category) {
        return categoryService.updateCategory(categoryId, category);
    }

    /**
     * 获取分类
     * <p>
     * 使用的case:修改的时候，获取一下。填充弹窗
     * 不获取也是可以的，从列表里获取数据
     * <p>
     * 权限：管理员权限
     *
     * @param categoryId
     * @return
     */
    @ApiOperation("获取分类")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{categoryId}")
    public ResponseResult getCategory(@PathVariable("categoryId") String categoryId) {
        return categoryService.getCategory(categoryId);
    }

    /**
     * 获取分类列表
     * <p>
     * 权限：管理员权限
     *
     * @return
     */
    @ApiOperation("获取分类列表")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listCategories() {
        return categoryService.listCategories();
    }
}
