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
@Table(name = "QSAUDITLOG")
public class QsAuditLog {
    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "DESCRIPTION", length = 4000)
    private String description;

    @Column(name = "EXECUTION_DATA", nullable = false)
    private LocalDate executionData;

}