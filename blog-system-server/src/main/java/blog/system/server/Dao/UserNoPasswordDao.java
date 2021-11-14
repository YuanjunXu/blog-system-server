package blog.system.server.dao;


import blog.system.server.pojo.SobUserNoPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserNoPasswordDao extends JpaRepository<SobUserNoPassword, String>, JpaSpecificationExecutor<SobUserNoPassword> {

}
