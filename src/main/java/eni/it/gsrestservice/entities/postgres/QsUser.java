package eni.it.gsrestservice.entities.postgres;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "qs_users")
public class QsUser {
    @Id
    @Column(name = "userid", nullable = false, length = 20)
    private String userid;

    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "user_is_active", nullable = false, length = 1)
    private String userIsActive;

    @Column(name = "data_last_modify", nullable = false)
    private OffsetDateTime dataLastModify;

}