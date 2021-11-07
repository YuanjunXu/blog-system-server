package blog.system.server.controller.admin;

import blog.system.server.entity.ArticleEntity;
import blog.system.server.interceptor.CheckTooFrequentCommit;
import blog.system.server.service.IArticleService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "文章相关")
@RestController
@RequestMapping("/admin/article")
public class ArticleAdminApi {

    @Autowired
    private IArticleService articleService;

    @ApiOperation("提交文章")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult postArticle(@RequestBody ArticleEntity article) {
        return articleService.postArticle(article);
    }

    /**
     * 如果是多用户，用户不可以删除，删除只是修改状态
     * 管理可以删除
     * <p>
     * 做成真的删除
     *
     * @param articleId
     * @return
     */
    @ApiOperation("删除文章")
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{articleId}")
    public ResponseResult deleteArticle(@PathVariable("articleId") String articleId) {
        return articleService.deleteArticleById(articleId);
    }

    @ApiOperation("编辑文章")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{articleId}")
    public ResponseResult updateArticle(@PathVariable("articleId") String articleId, @RequestBody ArticleEntity article) {
        return articleService.updateArticle(articleId, article);
    }

    @ApiOperation("文章详情")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId") String articleId) {
        return articleService.getArticleByIdForAdmin(articleId);
    }

    @ApiOperation("文章列表")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticles(@PathVariable("page") int page,
                                       @PathVariable("size") int size,
                                       @RequestParam(value = "state", required = false) String state,
                                       @RequestParam(value = "keyword", required = false) String keyword,
                                       @RequestParam(value = "categoryId", required = false) String categoryId) {
        return articleService.listArticles(page, size, keyword, categoryId, state);
    }

    @ApiOperation("提交文章")
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/sate/{articleId}")
    public ResponseResult deleteArticleByUpdateState(@PathVariable("articleId") String articleId) {
        return articleService.deleteArticleByState(articleId);
    }

    @ApiOperation("编辑文章状态：已删除, 正常")
    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{articleId}")
    public ResponseResult topArticle(@PathVariable("articleId") String articleId) {
        return articleService.topArticle(articleId);
    }

    @ApiOperation("文章数量")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/count")
    public ResponseResult getArticleCount() {
        return articleService.getArticleCount();
    }
}
