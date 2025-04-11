package eni.it.gsrestservice.db.repos.ora;


import eni.it.gsrestservice.entities.oracle.QsAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:47 </br>
 */
@Repository
public interface QsAdminUserRepository extends JpaRepository<QsAdminUser, Long> {
    QsAdminUser findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT AUTHENTICATED FROM QSADMINUSERS a WHERE a.USERNAME = :username", nativeQuery = true)
    String findByAuthenticatedAndUsername(String username);

    @Query(value = "select CURRENT_SESSION_LOGIN_TIME, SESSION_LOGIN_EXPIRE_TIME, AUTHENTICATED from QSADMINUSERS where USERNAME like :username", nativeQuery = true)
    QsAdminUser checkSession(String username);

    QsAdminUser findByUsername(String username);
}
