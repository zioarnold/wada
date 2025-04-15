package eni.it.gsrestservice.entities.postgres;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "qs_users")
public class QsUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_users_id_gen")
    @SequenceGenerator(name = "qs_users_id_gen", sequenceName = "qs_users_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "userid", nullable = false, length = 20)
    private String userId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "user_is_active", nullable = false, length = 1)
    private String userIsActive;

    @Column(name = "data_last_modify", nullable = false)
    private OffsetDateTime dataLastModify;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<QsUsersAttrib> qsUsersAttribs = new LinkedHashSet<>();

}