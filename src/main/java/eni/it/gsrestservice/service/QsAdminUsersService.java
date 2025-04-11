package eni.it.gsrestservice.service;


import eni.it.gsrestservice.db.repos.ora.QsAdminUserRepository;
import eni.it.gsrestservice.entities.oracle.QsAdminUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:53 </br>
 */
@Service
@RequiredArgsConstructor
public class QsAdminUsersService {
    private final QsAdminUserRepository qsAdminUserRepository;

    public List<QsAdminUser> findAllAdminUsers() {
        return qsAdminUserRepository.findAll();
    }

    public Optional<QsAdminUser> findById(Long id) {
        return qsAdminUserRepository.findById(id);
    }

    public QsAdminUser create(QsAdminUser qsAdminUser) {
        return qsAdminUserRepository.save(qsAdminUser);
    }

    public void deleteById(Long id) {
        qsAdminUserRepository.deleteById(id);
    }

    public QsAdminUser login(String username, String pwd) {
        return qsAdminUserRepository.findByUsernameAndPassword(username, pwd);
    }
}
