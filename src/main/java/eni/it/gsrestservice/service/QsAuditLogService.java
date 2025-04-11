package eni.it.gsrestservice.service;


import eni.it.gsrestservice.db.repos.ora.QsAuditLogRepository;
import eni.it.gsrestservice.entities.oracle.QsAuditLog;
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


    public List<QsAuditLog> findAll() {
        return qsAuditLogRepository.findAll();
    }
}
