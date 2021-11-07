package blog.system.server.service;

import blog.system.server.entity.CommentEntity;
import blog.system.server.utils.PageResult;
import blog.system.server.utils.ResponseResult;

public interface ICommentService {
    ResponseResult postComment(CommentEntity comment);

    ResponseResult<PageResult<CommentEntity>> listCommentByArticleId(String articleId, int page, int size);

    ResponseResult deleteCommentById(String commentId);

    ResponseResult<PageResult<CommentEntity>> listComments(int page, int size);

    ResponseResult topComment(String commentId);

    ResponseResult<Long> getCommentCount();
}
