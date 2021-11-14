package blog.system.server.controller.admin;

import blog.system.server.interceptor.CheckTooFrequentCommit;
import blog.system.server.pojo.Article;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/article")
public class ArticleAdminApi {

    @Autowired
    private IArticleService articleService;

    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult postArticle(@RequestBody Article article) {
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
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{articleId}")
    public ResponseResult deleteArticle(@PathVariable("articleId") String articleId) {
        return articleService.deleteArticleById(articleId);
    }

    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{articleId}")
    public ResponseResult updateArticle(@PathVariable("articleId") String articleId, @RequestBody Article article) {
        return articleService.updateArticle(articleId, article);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId") String articleId) {
        return articleService.getArticleByIdForAdmin(articleId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticles(@PathVariable("page") int page,
                                       @PathVariable("size") int size,
                                       @RequestParam(value = "state", required = false) String state,
                                       @RequestParam(value = "keyword", required = false) String keyword,
                                       @RequestParam(value = "categoryId", required = false) String categoryId) {
        return articleService.listArticles(page, size, keyword, categoryId, state);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/sate/{articleId}")
    public ResponseResult deleteArticleByUpdateState(@PathVariable("articleId") String articleId) {
        return articleService.deleteArticleByState(articleId);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{articleId}")
    public ResponseResult topArticle(@PathVariable("articleId") String articleId) {
        return articleService.topArticle(articleId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/count")
    public ResponseResult getArticleCount() {
        return articleService.getArticleCount();
    }
}
