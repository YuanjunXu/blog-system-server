package blog.system.server.dao;


import blog.system.server.pojo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenDao extends JpaRepository<RefreshToken, String>, JpaSpecificationExecutor<RefreshToken> {


    RefreshToken findOneByTokenKey(String tokenKey);

    RefreshToken findOneByMobileTokenKey(String mobileTokenKey);


    RefreshToken findOneByUserId(String userId);



    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `tb_refresh_token` SET `mobile_token_key` = '' WHERE `mobile_token_key` = ?")
    void deleteMobileTokenKey(String tokenKey);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `tb_refresh_token` SET `token_key` = '' WHERE `token_key` = ?")
    void deletePcTokenKey(String tokenKey);
}
