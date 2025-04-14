package eni.it.gsrestservice.entities.postgres;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "qs_dev_users_attrib")
public class QsDevUsersAttrib implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_dev_users_attrib_id_gen")
    @SequenceGenerator(name = "qs_dev_users_attrib_id_gen", sequenceName = "qs_dev_users_attrib_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private QsDevUser user;

    @Column(name = "userid", nullable = false, length = 20)
    private String userid;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Column(name = "value", nullable = false, length = 100)
    private String value;

    @Column(name = "data_last_modify", nullable = false)
    private OffsetDateTime dataLastModify;
}


