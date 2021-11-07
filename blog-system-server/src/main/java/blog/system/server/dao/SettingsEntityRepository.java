package blog.system.server.dao;


import blog.system.server.entity.SettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SettingsEntityRepository extends JpaRepository<SettingsEntity, String>, JpaSpecificationExecutor<SettingsEntity> {
    SettingsEntity findOneByKey(String key);
}