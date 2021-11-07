package blog.system.server.dao;


import blog.system.server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserEntityRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    /**
     * 根据用户查找用户
     *
     * @param userName
     * @return
     */
    UserEntity findOneByUserName(String userName);

    /**
     * 通过偶像查找
     *
     * @param email
     * @return
     */
    UserEntity findOneByEmail(String email);

    /**
     * 根據UserId 查找用户
     *
     * @param UserId
     * @return
     */
    UserEntity findOneById(String UserId);

    /**
     * 通过修改用户的状态来删除用户
     *
     * @param userId
     * @return
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `tb_user` SET `state` = '0' WHERE `id` = ?")
    int deleteUserByState(String userId);

    @Modifying
    @Query(nativeQuery = true, value = "update `tb_user` set `password` = ? where `email` = ?")
    int updatePasswordByEmail(String encode, String email);

    @Modifying
    @Query(nativeQuery = true, value = "update `tb_user` set `email` = ? where `id` = ?")
    int updateEmailById(String email, String id);
}