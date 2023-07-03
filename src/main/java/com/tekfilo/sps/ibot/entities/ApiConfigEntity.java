package com.tekfilo.sps.ibot.entities;

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
@Table(name = "tbl_api_user_config")
public class ApiConfigEntity {

    @Id
    @Column(name = "api_config_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "api_id")
    private Integer apiId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id", nullable = true, insertable = false, updatable = false)
    private ApiEntity api;


    @Column(name = "api_user_name")
    private String apiUserName;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "api_sid")
    private String apiSid;

    @Column(name = "tenant_uid")
    private String tenantUid;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "is_enabled")
    private Integer enabled;

    @Column(name = "cronexpression")
    private String cronexpression;

    @Column(name = "confirmation")
    private String confirmation;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;


    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer locked;

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
