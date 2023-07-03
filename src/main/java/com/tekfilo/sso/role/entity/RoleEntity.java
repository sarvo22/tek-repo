package com.tekfilo.sso.role.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_generator")
    @SequenceGenerator(name = "role_generator", sequenceName = "tbl_roles_seq", allocationSize = 1)
    @Column(name = "role_id", updatable = false, insertable = true, nullable = false)
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "subscription_id", insertable = true, updatable = false)
    private Integer subscriptionId;

    @Column(name = "company_id")
    private Integer companyId;


    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;

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
