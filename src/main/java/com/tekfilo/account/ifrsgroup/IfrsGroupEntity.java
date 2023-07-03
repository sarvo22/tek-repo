package com.tekfilo.account.ifrsgroup;

import com.tekfilo.account.ifrsschedule.IfrsScheduleEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_ifrs_group")
public class IfrsGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ifrsgroup_generator")
    @SequenceGenerator(name = "ifrsgroup_generator", sequenceName = "tbl_ifrs_group_seq", allocationSize = 1)
    @Column(name = "group_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_nature")
    private String groupNature;

    @Column(name = "dr_cr")
    private String debitCreditType;

    @Column(name = "description")
    private String description;

    @Column(name = "schedule_id")
    private Integer scheduleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "schedule_id", referencedColumnName = "schedule_id", insertable = false, updatable = false, nullable = true)
    private IfrsScheduleEntity schedule;

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
