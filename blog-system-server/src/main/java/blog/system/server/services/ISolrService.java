package blog.system.server.services;


import blog.system.server.pojo.Article;
import blog.system.server.response.ResponseResult;

public interface ISolrService {

    ResponseResult doSearch(String keyword, int page, int size, String categoryId, Integer sort);

    void addArticle(Article article);

    void deleteArticle(String articleId);

    void updateArticle(String articleId, Article article);
}
