package eni.it.gsrestservice.entities.oracle;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "QSFARMSTMP")
public class QsFarmsTmp {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(name = "FARMID", nullable = false)
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

}