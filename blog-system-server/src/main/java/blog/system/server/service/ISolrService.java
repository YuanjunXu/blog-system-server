package blog.system.server.service;

import blog.system.server.dao.SearchResultEntity;
import blog.system.server.entity.ArticleEntity;
import blog.system.server.utils.ResponseResult;

import java.util.List;

public interface ISolrService {

    ResponseResult<List<SearchResultEntity>> doSearch(String keyword, int page, int size, String categoryId, Integer sort);

    void addArticle(ArticleEntity article);

    void deleteArticle(String articleId);

    void updateArticle(String articleId, ArticleEntity article);
}
