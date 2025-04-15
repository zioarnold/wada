package eni.it.gsrestservice.entities.postgres;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "qs_admin_users")
public class QsAdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Column(name = "password", nullable = false, length = 40)
    private String password;

    @Column(name = "current_session_login_time", nullable = false)
    private LocalDate currentSessionLoginTime;

    @Column(name = "session_login_expire_time", nullable = false)
    private LocalDate sessionLoginExpireTime;

    @Column(name = "authenticated", nullable = false, length = 1)
    private String authenticated;

    @Column(name = "role", nullable = false, length = 5)
    private String role;

}