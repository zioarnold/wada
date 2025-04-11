package eni.it.gsrestservice.service.post;


import eni.it.gsrestservice.db.repos.post.QsUsersAttributesRepository;
import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 17:05 </br>
 */
@Service
@RequiredArgsConstructor
public class QsUsersAttributesService {
    private final QsUsersAttributesRepository qsUsersAttributesRepository;

    public List<QsUsersAttrib> findUserTypeByUserId(String userId) {
        return qsUsersAttributesRepository.findByUserid(userId);
    }

    public QsUsersAttrib updateRoleByUserId(String userId, String roleGroup, String oldRole, String userRole) {
        if (roleGroup.equalsIgnoreCase("ruolo")) {
            qsUsersAttributesRepository.update(userId.toUpperCase(), roleGroup, oldRole, userRole);
        } else {
            qsUsersAttributesRepository.update(userId.toUpperCase(), roleGroup, oldRole, userRole);
        }
    }
}
