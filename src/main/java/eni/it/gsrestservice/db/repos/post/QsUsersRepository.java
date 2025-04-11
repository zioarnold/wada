package eni.it.gsrestservice.db.repos.post;


import eni.it.gsrestservice.entities.postgres.QsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:40 </br>
 */
@Repository
public interface QsUsersRepository extends JpaRepository<QsUser, Long> {
}
