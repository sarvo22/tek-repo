package com.tekfilo.sso.plan.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_apps")
public class AppEntity {

    @Id
    @Column(name = "app_id", insertable = false, updatable = false)
    private Integer id;

    @Column(name = "app_name", insertable = false, updatable = false)
    private String appName;

    @Column(name = "app_desc", insertable = false, updatable = false)
    private String appDescription;

    @Column(name = "logo", insertable = false, updatable = false)
    private String logo;

    @Column(name = "sort_seq", insertable = false, updatable = false)
    private Integer sequence;

    @Column(name = "is_active", insertable = false, updatable = false)
    private Integer isActive;

    @Column(name = "is_locked", insertable = false, updatable = false)
    private Integer isLocked;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = false, updatable = false)
    private Timestamp createdDate;

    @Column(name = "is_deleted", insertable = false, updatable = false)
    private Integer isDeleted;

}
