package blog.system.server.dao;

import blog.system.server.entity.FriendsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FriendsEntityRepository extends JpaRepository<FriendsEntity, String>, JpaSpecificationExecutor<FriendsEntity> {
    FriendsEntity findOneById(String id);

    int deleteAllById(String friendLinkId);

    @Query(value = "select * from `tb_friends` where `state` = ? ", nativeQuery = true)
    List<FriendsEntity> listFriendLinkByState(String state);
}