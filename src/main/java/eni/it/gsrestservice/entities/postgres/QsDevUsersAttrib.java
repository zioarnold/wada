package eni.it.gsrestservice.entities.postgres;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "qs_dev_users_attrib")
public class QsDevUsersAttrib implements Serializable {
    @Id
    @SequenceGenerator(name = "qs_dev_users_attrib_id_gen", sequenceName = "qs_dev_users_attrib_seq", allocationSize = 1)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private QsDevUser id;

    @Column(name = "userid", nullable = false, length = 20)
    private String userid;

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