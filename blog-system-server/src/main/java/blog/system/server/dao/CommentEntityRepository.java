package blog.system.server.dao;


import blog.system.server.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CommentEntityRepository extends JpaRepository<CommentEntity, String>, JpaSpecificationExecutor<CommentEntity> {
    CommentEntity findOneById(String commentId);

    int deleteAllByArticleId(String articleId);

    Page<CommentEntity> findAllByArticleId(String articleId, Pageable pageable);
}