package blog.system.server.service;


import blog.system.server.entity.ArticleEntity;
import blog.system.server.entity.ArticleNoContentEntity;
import blog.system.server.entity.LabelEntity;
import blog.system.server.utils.PageResult;
import blog.system.server.utils.ResponseResult;

import java.util.List;

public interface IArticleService {
    ResponseResult postArticle(ArticleEntity article);

    ResponseResult<PageResult<ArticleNoContentEntity>> listArticles(int page, int size, String keyword,
                                                                    String categoryId, String state);

    ResponseResult<ArticleEntity> getArticleById(String articleId);

    ResponseResult updateArticle(String articleId, ArticleEntity article);

    ResponseResult deleteArticleById(String articleId);

    ResponseResult deleteArticleByState(String articleId);

    ResponseResult topArticle(String articleId);

    ResponseResult<ArticleEntity> getArticleByIdForAdmin(String articleId);

    ResponseResult<List<ArticleNoContentEntity>> listTopArticles();

    ResponseResult<List<ArticleNoContentEntity>> listRecommendArticle(String articleId, int size);

    ResponseResult<PageResult<ArticleNoContentEntity>> listArticlesByLabel(int page, int size, String label);

    ResponseResult<PageResult<LabelEntity>> listLabels(int size);

    ResponseResult<Long> getArticleCount();
}
