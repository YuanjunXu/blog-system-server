package blog.system.server.dao;


import blog.system.server.pojo.FriendLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendLinkDao extends JpaSpecificationExecutor<FriendLink>, JpaRepository<FriendLink, String> {
    FriendLink findOneById(String id);

    int deleteAllById(String friendLinkId);

    @Query(value = "select * from `tb_friends` where `state` = ? ", nativeQuery = true)
    List<FriendLink> listFriendLinkByState(String state);
}
