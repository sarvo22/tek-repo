package com.tekfilo.sso.plan.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_company_subscription_map")
public class CompanySubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compsub_generator")
    @SequenceGenerator(name = "compsub_generator", sequenceName = "tbl_company_subscription_map_seq", allocationSize = 1)
    @Column(name = "map_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "subscription_id")
    private Integer subscriptionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_id", referencedColumnName = "subscription_id", insertable = false, updatable = false)
    private Subscription subscription;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
