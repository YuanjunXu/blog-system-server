package blog.system.server.dao;


import blog.system.server.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface ArticleEntityRepository extends JpaRepository<ArticleEntity, String>, JpaSpecificationExecutor<ArticleEntity> {
    ArticleEntity findOneById(String id);

    @Modifying
    int deleteAllById(String articleId);

    @Modifying
    @Query(nativeQuery = true, value = "update `tb_article` set `state` = '0' where `id` = ? ")
    int deleteArticleByState(String articleId);

    @Query(nativeQuery = true, value = "select `labels` from `tb_article` where `id` = ?")
    String listArticleLabelsById(String articleId);
}