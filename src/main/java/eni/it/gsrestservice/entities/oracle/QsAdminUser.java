package eni.it.gsrestservice.entities.oracle;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "QSADMINUSERS")
public class QsAdminUser {
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 30)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 40)
    private String password;

    @Column(name = "CURRENT_SESSION_LOGIN_TIME", nullable = false)
    private LocalDate currentSessionLoginTime;

    @Column(name = "SESSION_LOGIN_EXPIRE_TIME", nullable = false)
    private LocalDate sessionLoginExpireTime;

    @Column(name = "AUTHENTICATED", nullable = false, length = 1)
    private String authenticated;

    @Column(name = "ROLE", nullable = false, length = 5)
    private String role;

}