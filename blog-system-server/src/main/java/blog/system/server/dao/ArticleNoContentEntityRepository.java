package blog.system.server.dao;

import blog.system.server.entity.ArticleNoContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleNoContentEntityRepository extends JpaRepository<ArticleNoContentEntity, String>, JpaSpecificationExecutor<ArticleNoContentEntity> {
    ArticleNoContentEntity findOneById(String id);


    @Query(nativeQuery = true, value = "SELECT * FROM `tb_article` WHERE `labels` LIKE ? AND `id` != ? AND (`state` = '1' OR `state` = '3') LIMIT ?")
    List<ArticleNoContentEntity> listArticleByLikeLabel(String label, String originalArticleId, int size);

    @Query(nativeQuery = true, value = "SELECT * FROM `tb_article` WHERE `id` != ? AND (`state` = '1' OR `state` = '3') ORDER BY `create_time` DESC LIMIT ?")
    List<ArticleNoContentEntity> listLastedArticleBySize(String originalArticleId, int size);

}
