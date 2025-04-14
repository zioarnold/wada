package eni.it.gsrestservice.entities.postgres;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "qs_farms_tmp")
@ToString
@RequiredArgsConstructor
public class QsFarmsTmp {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long farmId;

    @Column(name = "DESCRIZIONE", nullable = false, length = 100)
    private String description;

    @Column(name = "DBUSER", nullable = false, length = 30)
    private String dbUser;

    @Column(name = "DBPASSWORD", nullable = false, length = 30)
    private String dbPassword;

    @Column(name = "DBHOST", nullable = false, length = 100)
    private String dbHost;

    @Column(name = "QSHOST", nullable = false, length = 100)
    private String qsHost;

    @Column(name = "QSPATHCLIENT", nullable = false, length = 200)
    private String qsPathClient;

    @Column(name = "QSPATHROOT", nullable = false, length = 200)
    private String qsPathRoot;

    @Column(name = "QSXRFKEY", nullable = false, length = 200)
    private String qsXrfKey;

    @Column(name = "QSKSPASSWD", nullable = false, length = 30)
    private String qsKsPasswd;

    @Column(name = "NOTE", length = 100)
    private String note;

    @Column(name = "DATALASTMODIFY")
    private LocalDate dateLastModify;

    @Column(name = "DBSID", nullable = false, length = 30)
    private String dbSid;

    @Column(name = "DBPORT", nullable = false, length = 10)
    private String dbPort;

    @Column(name = "QSUSERHEADER", nullable = false, length = 100)
    private String qsUserHeader;

    @Column(name = "ENVIRONMENT", nullable = false, length = 5)
    private String environment;

    @Column(name = "CAME", nullable = false, length = 30)
    private String came;

    @Column(name = "QSRELOADTASKNAME", length = 100)
    private String qsReloadTaskName;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        QsFarmsTmp that = (QsFarmsTmp) o;
        return getFarmId() != null && Objects.equals(getFarmId(), that.getFarmId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}