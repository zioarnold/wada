package eni.it.gsrestservice.service.post;


import eni.it.gsrestservice.db.repos.post.QsUsersRepository;
import eni.it.gsrestservice.entities.postgres.QsUser;
import eni.it.gsrestservice.entities.postgres.QsUsersAttrMapper;
import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 16:20 </br>
 */
@Service
@RequiredArgsConstructor
public class QsUsersService {
    private final QsUsersRepository qsUsersRepository;
    private final QsUsersAttributesService qsUsersAttributesService;
    private final QsUsersAttrMapper mapper;

    public QsUser findById(Long id) {
        return qsUsersRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Unable to find user with id:" + id));
    }

    public QsUser findByUserId(String id) {
        return qsUsersRepository.findByUserid(id);
    }

    public QsUsersAttrMapper findUserRoleByUserID(String id) {
        QsUser byUserid = qsUsersRepository.findByUserid(id);
        QsUsersAttrib userTypeByUserId = qsUsersAttributesService.findByUserId(id);
        return mapper.map(byUserid, userTypeByUserId);
    }

    public long getTotalUsers() {
        return qsUsersRepository.count();
    }

    public List<QsUser> findAll() {
        return qsUsersRepository.findAll();
    }

    public QsUser create(QsUser qsUser) {
        if (findByUserId(qsUser.getUserid()) == null) {
            return qsUsersRepository.save(qsUser);
        }
        return null;
    }

    public boolean deleteById(String id) {
        try {
            qsUsersRepository.deleteUserID(id.toUpperCase());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean disableUserById(String userId, String disableYN) {
        QsUser byUserId = findByUserId(userId);
        if (byUserId != null) {
            byUserId.setUserIsActive(disableYN);
            qsUsersRepository.save(byUserId);
            return true;
        }
        return false;
    }
}
