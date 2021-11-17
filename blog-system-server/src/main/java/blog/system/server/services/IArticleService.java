package blog.system.server.services;

import blog.system.server.pojo.Article;
import blog.system.server.response.ResponseResult;

public interface IArticleService {
    ResponseResult postArticle(Article article);

    ResponseResult listArticles(int page, int size, String keyword,
                                String categoryId, String state);

    ResponseResult getArticleById(String articleId);
    ResponseResult getArticleByIdForUserEdit(String articleId);

    ResponseResult updateArticle(String articleId, Article article);

    ResponseResult deleteArticleById(String articleId);

    ResponseResult deleteArticleByState(String articleId);

    ResponseResult topArticle(String articleId);
    ResponseResult getArticleByIdForAdmin(String articleId);

    ResponseResult listTopArticles();

    ResponseResult listRecommendArticle(String articleId, int size);

    ResponseResult listArticlesByLabel(int page, int size, String label);

    ResponseResult listLabels(int size);

    ResponseResult getArticleCount();
}
