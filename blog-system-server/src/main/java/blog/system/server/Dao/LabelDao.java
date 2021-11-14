package blog.system.server.dao;


import blog.system.server.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LabelDao extends JpaRepository<Label, String>, JpaSpecificationExecutor<Label> {


    @Modifying
    int deleteOneById(String id);

    @Modifying
    @Query(value = "DELETE FROM `tb_labels` WHERE id = ?", nativeQuery = true)
    int customDeleteLabelById(String id);

    /**
     * 根据ID查找一个标签
     *
     * @param id
     * @return
     */
    Label findOneById(String id);

    Label findOneByName(String name);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `tb_labels`  SET `count` = `count` + 1 WHERE `name` = ?")
    int updateCountByName(String labelName);

}
