package com.tekfilo.admin.department;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_department")
public class DepartmentEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_generator")
    @SequenceGenerator(name = "department_generator", sequenceName = "tbl_department_seq", allocationSize = 1)
    @Column(name = "department_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "department_name")
    private String departmentName;

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
