package com.tekfilo.sso.plan.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_subscription")
public class Subscription {

    @Id
    @Column(name = "subscription_id", insertable = false, updatable = false)
    private Integer id;

    @Column(name = "subscription_name", insertable = false, updatable = false)
    private String subscriptionName;

    @Column(name = "subscription_desc", insertable = false, updatable = false)
    private String subscriptionDesc;

    @Column(name = "subscription_type", insertable = false, updatable = false)
    private String subscriptionType;

    @Column(name = "app_id")
    private Integer appId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "app_id", referencedColumnName = "app_id", insertable = false, updatable = false, nullable = false)
    private AppEntity app;

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
