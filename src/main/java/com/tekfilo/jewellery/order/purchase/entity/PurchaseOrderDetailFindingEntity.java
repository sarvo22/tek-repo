package com.tekfilo.jewellery.order.purchase.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_jew_pod_finding")
public class PurchaseOrderDetailFindingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jewpodfinding_generator")
    @SequenceGenerator(name = "jewpodfinding_generator", sequenceName = "tbl_jew_pod_finding_seq", allocationSize = 1)
    @Column(name = "finding_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "purchase_order_det_id", insertable = true,updatable = false)
    private Integer purchaseOrderDetailId;


    @Column(name = "purchase_order_id", insertable = true,updatable = false)
    private Integer purchaseOrderId;


    @Column(name = "finding_name", insertable = true)
    private String findingName;

    @Column(name = "description")
    private String description;

    @Column(name = "qty")
    private Double qty;

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
