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
@Table(name = "qs_audit_log")
public class QsAuditLog {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "execution_data", nullable = false)
    private OffsetDateTime executionData;

}