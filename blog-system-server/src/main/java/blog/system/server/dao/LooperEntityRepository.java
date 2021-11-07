package blog.system.server.dao;


import blog.system.server.entity.LooperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LooperEntityRepository extends JpaRepository<LooperEntity, String>, JpaSpecificationExecutor<LooperEntity> {
    LooperEntity findOneById(String loopId);

    @Query(nativeQuery = true, value = "select * from `tb_looper` where `state` = ?")
    List<LooperEntity> listLoopByState(String state);
}