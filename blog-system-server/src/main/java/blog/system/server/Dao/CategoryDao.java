package blog.system.server.dao;
import blog.system.server.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {
    Category findOneById(String id);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `tb_categories` SET `status` = '0' WHERE `id` = ?")
    int deleteCategoryByUpdateState(String categoryId);

    @Query(nativeQuery = true, value = "select * from `tb_categories` where `status` = ? order by `create_time` DESC")
    List<Category> listCategoriesByState(String status);
}
