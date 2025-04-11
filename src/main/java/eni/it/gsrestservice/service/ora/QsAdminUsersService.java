package eni.it.gsrestservice.service.ora;


import eni.it.gsrestservice.db.repos.ora.QsAdminUserRepository;
import eni.it.gsrestservice.entities.oracle.QsAdminUser;
import eni.it.gsrestservice.model.QsAdminUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:53 </br>
 */
@Service
@RequiredArgsConstructor
public class QsAdminUsersService {
    private static boolean authenticated = false;

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

    public QsAdminUser update(QsAdminUser qsAdminUser) {
        return qsAdminUserRepository.save(qsAdminUser);
    }

    public QsAdminUser login(String username, String pwd) {
        QsAdminUser user = qsAdminUserRepository.findByUsernameAndPassword(username, pwd);
        user.setAuthenticated("Y");
        QsAdminUsers.username = user.getUsername();
        QsAdminUsers.role = user.getRole();
        return user;
    }

    public boolean logout(String username) {
        QsAdminUser user = qsAdminUserRepository.findByUsername(username);
        user.setAuthenticated("N");
        update(user);
        return true;
    }

    public boolean isAuthenticated(String username) {
        authenticated = true;
        return Objects.equals(qsAdminUserRepository.findByAuthenticatedAndUsername(username), "Y");
    }

    public int checkSession(String username) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date currentTime = new Date(System.currentTimeMillis());
        String format = simpleDateFormat.format(currentTime);
        QsAdminUser qsAdminUser = qsAdminUserRepository.checkSession(username);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String session_login_expire_time = simpleDateFormat1.format(qsAdminUser.getSessionLoginExpireTime());
        return format.compareTo(session_login_expire_time);
    }
}
