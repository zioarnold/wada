package eni.it.gsrestservice.db.repos.post;


import eni.it.gsrestservice.entities.postgres.QsDevUsersAttrib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:39 </br>
 */
@Repository
public interface QsDevUsersAttributesRepository extends JpaRepository<QsDevUsersAttrib, Long> {
}
