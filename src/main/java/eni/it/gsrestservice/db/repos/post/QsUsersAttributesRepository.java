package eni.it.gsrestservice.db.repos.post;


import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:40 </br>
 */
@Repository
public interface QsUsersAttributesRepository extends JpaRepository<QsUsersAttrib, Long> {
    @Query(value = "SELECT userid,type,value FROM qs_dev_users_attrib WHERE userid = :userId", nativeQuery = true)
    List<QsUsersAttrib> findByUserid(String userid);

    @Query(value = "UPDATE qs_dev_users_attrib set value =:userRole WHERE value = :oldRole and type ='gruppo' and userid = :userId", nativeQuery = true)
    void update(String userId, String roleGroup, String oldRole, String userRole);
}
