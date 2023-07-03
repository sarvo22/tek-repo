package com.tekfilo.account.subgroup;

import com.tekfilo.account.group.GroupEntity;
import com.tekfilo.account.schedule.ScheduleEntity;
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
@Table(name = "tbl_sub_group")
public class SubGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subgroup_generator")
    @SequenceGenerator(name = "subgroup_generator", sequenceName = "tbl_sub_group_seq", allocationSize = 1)
    @Column(name = "subgroup_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "subgroup_name")
    private String subGroupName;

    @Column(name = "subgroup_nature")
    private String subGroupNature;

    @Column(name = "subgroup_dr_cr")
    private String debitCreditType;

    @Column(name = "description")
    private String description;

    @Column(name = "group_id")
    private Integer groupId;

    @OneToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id", insertable = false, updatable = false, nullable = true)
    private GroupEntity group;

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
