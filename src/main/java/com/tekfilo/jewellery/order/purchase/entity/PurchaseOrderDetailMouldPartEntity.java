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
@Table(name = "tbl_jew_pod_mould_part")
public class PurchaseOrderDetailMouldPartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jewpodmouldpart_generator")
    @SequenceGenerator(name = "jewpodmouldpart_generator", sequenceName = "tbl_jew_pod_mould_part_seq", allocationSize = 1)
    @Column(name = "mould_part_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "purchase_order_det_id", insertable = true,updatable = false)
    private Integer purchaseOrderDetailId;


    @Column(name = "purchase_order_id", insertable = true,updatable = false)
    private Integer purchaseOrderId;


    @Column(name = "part_name")
    private String partName;

    @Column(name = "part_no")
    private String partNo;

    @Column(name = "description")
    private String description;

    @Column(name = "pieces")
    private Double pieces;

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
