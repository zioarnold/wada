package eni.it.gsrestservice.repos.post;


import eni.it.gsrestservice.entities.postgres.QsAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:47 </br>
 */
@Repository
public interface QsAuditLogRepository extends JpaRepository<QsAuditLog, String> {
}
