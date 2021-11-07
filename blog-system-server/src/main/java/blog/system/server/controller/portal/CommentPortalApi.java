package blog.system.server.controller.portal;

import blog.system.server.entity.CommentEntity;
import blog.system.server.interceptor.CheckTooFrequentCommit;
import blog.system.server.service.ICommentService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "评论相关")
@RestController
@RequestMapping("/portal/comment")
public class CommentPortalApi {

    @Autowired
    private ICommentService commentService;

    @ApiOperation("提交评论")
    @CheckTooFrequentCommit
    @PostMapping
    public ResponseResult postComment(@RequestBody CommentEntity comment) {
        return commentService.postComment(comment);
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId") String commentId) {
        return commentService.deleteCommentById(commentId);
    }

    @ApiOperation("评论列表")
    @GetMapping("/list/{articleId}/{page}/{size}")
    public ResponseResult listComments(@PathVariable("articleId") String articleId, @PathVariable("page") int page,
                                       @PathVariable("size") int size) {
        return commentService.listCommentByArticleId(articleId, page, size);
    }
}
