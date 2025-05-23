package eni.it.gsrestservice.service.post;


import eni.it.gsrestservice.entities.postgres.QsAuditLog;
import eni.it.gsrestservice.repos.post.QsAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 15:26 </br>
 */
@Service
@RequiredArgsConstructor
public class QsAuditLogService {
    private final QsAuditLogRepository qsAuditLogRepository;

    public QsAuditLog save(QsAuditLog qsAuditLog) {
        return qsAuditLogRepository.save(qsAuditLog);
    }

    public List<QsAuditLog> findAll() {
        return qsAuditLogRepository.findAll();
    }
}
