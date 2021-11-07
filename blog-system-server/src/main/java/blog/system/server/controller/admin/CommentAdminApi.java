package blog.system.server.controller.admin;

import blog.system.server.service.ICommentService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "评论相关")
@RestController
@RequestMapping("/admin/comment")
public class CommentAdminApi {

    @Autowired
    private ICommentService commentService;

    @ApiOperation("删除评论")
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId") String commentId) {
        return commentService.deleteCommentById(commentId);
    }

    @ApiOperation("评论列表")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listComments(@PathVariable("page") int page, @PathVariable("size") int size) {
        return commentService.listComments(page, size);
    }

    @ApiOperation("置顶评论")
    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{commentId}")
    public ResponseResult topComment(@PathVariable("commentId") String commentId) {
        return commentService.topComment(commentId);
    }

    @ApiOperation("评论数量")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/count")
    public ResponseResult getCommentCount() {
        return commentService.getCommentCount();
    }


}
