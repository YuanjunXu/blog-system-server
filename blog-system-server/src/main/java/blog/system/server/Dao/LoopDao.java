package blog.system.server.dao;


import blog.system.server.pojo.Looper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoopDao extends JpaSpecificationExecutor<Looper>, JpaRepository<Looper, String> {

    Looper findOneById(String loopId);

    @Query(nativeQuery = true, value = "select * from `tb_looper` where `state` = ?")
    List<Looper> listLoopByState(String state);
}
