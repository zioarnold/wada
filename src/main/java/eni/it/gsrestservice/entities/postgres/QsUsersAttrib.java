package eni.it.gsrestservice.entities.postgres;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "qs_users_attrib")
public class QsUsersAttrib implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qs_users_attrib_id_gen")
    @SequenceGenerator(name = "qs_users_attrib_id_gen", sequenceName = "qs_users_attrib_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private QsUser user;

    @Column(name = "userid", nullable = false, length = 20, insertable = false, updatable = false)
    private String userId;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Column(name = "value", nullable = false, length = 100)
    private String value;

    @Column(name = "data_last_modify", nullable = false)
    private OffsetDateTime dataLastModify;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}