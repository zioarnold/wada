package eni.it.gsrestservice.entities.oracle;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "QSFARMSTMP")
public class QsFarmsTmp {
    @Column(name = "FARMID", nullable = false)
    private Long farmid;

    @Column(name = "DESCRIZIONE", nullable = false, length = 100)
    private String descrizione;

    @Column(name = "DBUSER", nullable = false, length = 30)
    private String dbuser;

    @Column(name = "DBPASSWORD", nullable = false, length = 30)
    private String dbpassword;

    @Column(name = "DBHOST", nullable = false, length = 100)
    private String dbhost;

    @Column(name = "QSHOST", nullable = false, length = 100)
    private String qshost;

    @Column(name = "QSPATHCLIENT", nullable = false, length = 200)
    private String qspathclient;

    @Column(name = "QSPATHROOT", nullable = false, length = 200)
    private String qspathroot;

    @Column(name = "QSXRFKEY", nullable = false, length = 200)
    private String qsxrfkey;

    @Column(name = "QSKSPASSWD", nullable = false, length = 30)
    private String qskspasswd;

    @Column(name = "NOTE", length = 100)
    private String note;

    @Column(name = "DATALASTMODIFY")
    private LocalDate datalastmodify;

    @Column(name = "DBSID", nullable = false, length = 30)
    private String dbsid;

    @Column(name = "DBPORT", nullable = false, length = 10)
    private String dbport;

    @Column(name = "QSUSERHEADER", nullable = false, length = 100)
    private String qsuserheader;

    @Column(name = "ENVIRONMENT", nullable = false, length = 5)
    private String environment;

    @Column(name = "CAME", nullable = false, length = 30)
    private String came;

    @Column(name = "QSRELOADTASKNAME", length = 100)
    private String qsreloadtaskname;

}