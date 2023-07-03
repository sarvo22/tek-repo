package com.tekfilo.admin.process;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_process")
public class ProcessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "process_generator")
    @SequenceGenerator(name = "process_generator", sequenceName = "tbl_process_seq", allocationSize = 1)
    @Column(name = "process_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "process_name")
    private String processName;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "created_by", insertable = true, updatable = false)
    private Integer createdBy;
    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @Column(name = "modified_by", insertable = false, updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
