package com.tekfilo.account.costcategory;

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
@Table(name = "tbl_cost_category")
public class CostCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "costcategory_generator")
    @SequenceGenerator(name = "costcategory_generator", sequenceName = "tbl_cost_category_seq", allocationSize = 1)
    @Column(name = "cost_category_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "cost_category_name")
    private String costCategoryName;

    @Column(name = "description")
    private String description;

    @Column(name = "cost_category_group")
    private String costCategoryGroup;

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
