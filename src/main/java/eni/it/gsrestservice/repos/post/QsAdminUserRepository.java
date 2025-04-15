package eni.it.gsrestservice.repos.post;


import eni.it.gsrestservice.entities.postgres.QsAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:47 </br>
 */
@Repository
public interface QsAdminUserRepository extends JpaRepository<QsAdminUser, Long> {
    @Query(value = "select * from qs_admin_users where username = :username and password = :password", nativeQuery = true)
    QsAdminUser findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT authenticated FROM qs_admin_users a WHERE a.username = :username", nativeQuery = true)
    String findByAuthenticatedAndUsername(String username);

    @Query(value = "select CURRENT_SESSION_LOGIN_TIME, SESSION_LOGIN_EXPIRE_TIME, AUTHENTICATED from qs_admin_users where USERNAME like :username", nativeQuery = true)
    QsAdminUser checkSession(String username);

    QsAdminUser findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE qs_admin_users a SET a.PASSWORD = :password WHERE a.ID = :adminId", nativeQuery = true)
    void updatePasswordByUsername(Long adminId, String password);
}
