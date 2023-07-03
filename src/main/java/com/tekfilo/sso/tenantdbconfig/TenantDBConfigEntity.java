package com.tekfilo.sso.tenantdbconfig;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_user_tenant")
public class TenantDBConfigEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenantdb_generator")
    @SequenceGenerator(name = "tenantdb_generator", sequenceName = "tbl_user_tenant_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, insertable = true, nullable = false)
    private Integer Id;

    @Column(name = "user_uid")
    private String userUID;

    @Column(name = "tenant_uid")
    private String tenantUID;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "remarks")
    private Integer remarks;

    @Column(name = "system_remarks")
    private Integer systemRemarks;

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


