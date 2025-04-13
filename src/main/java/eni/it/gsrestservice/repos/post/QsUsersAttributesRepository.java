package eni.it.gsrestservice.repos.post;


import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:40 </br>
 */
@Repository
public interface QsUsersAttributesRepository extends JpaRepository<QsUsersAttrib, Long> {

    @Query(value = "SELECT userid,type,value FROM qs_dev_users_attrib WHERE userid = :userId", nativeQuery = true)
    QsUsersAttrib findByUserId(String userId);

    @Modifying
    @Query(value = "DELETE FROM qs_users_attrib WHERE userid = :userId AND type = :type AND value = :value", nativeQuery = true)
    void deleteRoleGroupByUserID(String userId, String type, String value);

    @Modifying
    @Query(value = "UPDATE qs_users_attrib set value = :newRole WHERE value = :oldRole AND userid = :userId", nativeQuery = true)
    void updateUserRole(String userId, String newRole, String oldRole);

    long countByType(String type);

    @Transactional
    @Modifying
    @Query("update QsUsersAttrib q set q.value = ?3 where q.userId = ?2 and q.value = ?2")
    void updateTypeAndUseridByValue(String type, String userid, String value);
}
