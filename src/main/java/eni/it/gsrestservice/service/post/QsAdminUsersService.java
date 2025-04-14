package eni.it.gsrestservice.service.post;


import eni.it.gsrestservice.entities.postgres.QsAdminUser;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.repos.post.QsAdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static eni.it.gsrestservice.utility.Utility.MD5;

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

    public QsAdminUser update(QsAdminUser qsAdminUser) {
        return qsAdminUserRepository.save(qsAdminUser);
    }

    public QsAdminUser login(String username, String pwd) {
        QsAdminUser user = qsAdminUserRepository.findByUsernameAndPassword(username, pwd);
        user.setAuthenticated("Y");
        user.setCurrentSessionLoginTime(LocalDate.now());
        user.setSessionLoginExpireTime(LocalDate.now().plusDays(1));
        QsAdminUsers.username = user.getUsername();
        QsAdminUsers.role = user.getRole();
        System.out.println("user.toString() = " + user);
        qsAdminUserRepository.save(user);
        return user;
    }

    public boolean logout(String username) {
        QsAdminUser user = qsAdminUserRepository.findByUsername(username);
        user.setAuthenticated("N");
        update(user);
        return true;
    }

    public boolean isAuthenticated(String username) {
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

    public boolean resetPassword(Long adminId, String password) {
        try {
            qsAdminUserRepository.updatePasswordByUsername(adminId, MD5(password));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
