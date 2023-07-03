package com.tekfilo.account.ifrsschedule;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_ifrs_schedule")
public class IfrsScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ifrsschedule_generator")
    @SequenceGenerator(name = "ifrsschedule_generator", sequenceName = "tbl_ifrs_schedule_seq", allocationSize = 1)
    @Column(name = "schedule_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "schedule_name")
    private String scheduleName;

    @Column(name = "schedule_nature")
    private String scheduleNature;

    @Column(name = "dr_cr")
    private String debitCreditType;

    @Column(name = "description")
    private String description;

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
    private Timestamp createdDt;

    @Column(name = "modified_by", insertable = false, updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDt;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
