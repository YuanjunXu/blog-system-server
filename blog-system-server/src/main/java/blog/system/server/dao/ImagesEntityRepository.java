package blog.system.server.dao;


import blog.system.server.entity.ImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface ImagesEntityRepository extends JpaRepository<ImagesEntity, String>, JpaSpecificationExecutor<ImagesEntity> {
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `tb_images` SET `state` = '0' WHERE id = ?")
    int deleteImageByUpdateState(String imageId);
}