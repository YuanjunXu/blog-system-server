package blog.system.server.dao;

import blog.system.server.entity.DailyViewCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface DailyViewCountEntityRepository extends JpaRepository<DailyViewCountEntity, String>, JpaSpecificationExecutor<DailyViewCountEntity> {

}