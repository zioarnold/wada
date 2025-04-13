package eni.it.gsrestservice.repos.post;


import eni.it.gsrestservice.entities.postgres.QsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:40 </br>
 */
@Repository
public interface QsUsersRepository extends JpaRepository<QsUser, Long> {
    @Modifying
    @Query(value = "DELETE FROM qs_users WHERE userid = :userId", nativeQuery = true)
    void deleteUserID(String userId);

    QsUser findByUserId(String userid);

    QsUser findUserRoleByUserId(String userid);
}
