package eni.it.gsrestservice.service.post;


import eni.it.gsrestservice.repos.post.QsUsersAttributesRepository;
import eni.it.gsrestservice.entities.oracle.QsAuditLog;
import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.ora.QsAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 17:05 </br>
 */
@Service
@RequiredArgsConstructor
public class QsUsersAttributesService {
    private final QsUsersAttributesRepository qsUsersAttributesRepository;
    private final QsAuditLogService qsAuditLogService;

    public List<QsUsersAttrib> findUserTypeByUserId(String userId) {
        return qsUsersAttributesRepository.findByUserid(userId);
    }

    public boolean updateRoleByUserId(String userId, String roleGroup, String oldRole, String userRole) {
        switch (roleGroup) {
            case "gruppo":
                qsUsersAttributesRepository.update(userId.toUpperCase(), "gruppo", oldRole, userRole);
                break;

            case "ruolo":
                qsUsersAttributesRepository.update(userId.toUpperCase(), "ruolo", oldRole, userRole);
                break;
        }
        return true;
    }

    public boolean deleteUserRoleByUserId(String userId, String type, String value) {
        try {
            qsUsersAttributesRepository.deleteRoleGroupByUserID(userId, type, value);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Error deleting user role", e);
        }
    }

    public void createOrUpdate(QsUsersAttrib qsUsersAttrib) {
        long type = qsUsersAttributesRepository.countByType(qsUsersAttrib.getType());
        if (type <= 0) {
            qsUsersAttributesRepository.save(qsUsersAttrib);
        } else {
            qsUsersAttributesRepository.updateTypeAndUseridByValue(qsUsersAttrib.getType(), qsUsersAttrib.getUserid(), qsUsersAttrib.getValue());
        }
    }

    public void create(QsUsersAttrib qsUsersAttrib, String tabAttribute) {
        qsUsersAttributesRepository.save(qsUsersAttrib);
        QsAuditLog qsAuditLog = new QsAuditLog();
        qsAuditLog.setDescription("Utenza " + QsAdminUsers.username + " su questa farm: " +
                                  Farm.description + " di " + Farm.environment + " ha eseguito questa query: INSERT INTO "
                                  + tabAttribute + " (userid, type, value) VALUES("
                                  + qsUsersAttrib.getUserid() + "," + qsUsersAttrib.getType() + "," + qsUsersAttrib.getValue() + ")");
        qsAuditLog.setExecutionData(LocalDate.now());
        qsAuditLogService.save(qsAuditLog);
    }

    public boolean updateUserRole(String userId, String oldRole, String userRoleByUserId, String tabAttribute) {
        try {
            qsUsersAttributesRepository.updateUserRole(userId, oldRole, userRoleByUserId);
            QsAuditLog qsAuditLog = new QsAuditLog();
            qsAuditLog.setDescription("Utenza " + QsAdminUsers.username + " su questa farm: " +
                                      Farm.description + " di " + Farm.environment + " ha eseguito questa query: update " +
                                      tabAttribute + " set value = " + userRoleByUserId +
                                      " where value like " + oldRole + " and type like ruolo and userid like " + userId.toUpperCase());
            qsAuditLog.setExecutionData(LocalDate.now());
            qsAuditLogService.save(qsAuditLog);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Error updating user role", e);
        }
    }

    public QsUsersAttrib findByUserId(String id) {
        return qsUsersAttributesRepository.findByUserId(id);
    }
}
