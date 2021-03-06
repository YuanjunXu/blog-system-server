package blog.system.server.services;


import blog.system.server.pojo.Comment;
import blog.system.server.response.ResponseResult;

public interface ICommentService {
    ResponseResult postComment(Comment comment);

    ResponseResult listCommentByArticleId(String articleId, int page, int size);

    ResponseResult deleteCommentById(String commentId);

    ResponseResult listComments(int page, int size);

    ResponseResult topComment(String commentId);

    ResponseResult getCommentCount();
}
