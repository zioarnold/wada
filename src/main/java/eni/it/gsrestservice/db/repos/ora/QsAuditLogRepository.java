package eni.it.gsrestservice.db.repos.ora;


import eni.it.gsrestservice.entities.oracle.QsAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:47 </br>
 */
@Repository
public interface QsAuditLogRepository extends JpaRepository<QsAuditLog, Long> {
}
