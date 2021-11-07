package blog.system.server.dao;

import blog.system.server.entity.UserNoPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserNoPasswordEntityRepository extends JpaRepository<UserNoPasswordEntity, String>, JpaSpecificationExecutor<UserNoPasswordEntity> {

}
