package com.tekfilo.jewellery.order.purchase.entity;

import com.tekfilo.jewellery.master.KaratageEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_jew_pod_wax")
public class PurchaseOrderDetailWaxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jewpodwax_generator")
    @SequenceGenerator(name = "jewpodwax_generator", sequenceName = "tbl_jew_pod_wax_seq", allocationSize = 1)
    @Column(name = "wax_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "purchase_order_det_id", insertable = true,updatable = false)
    private Integer purchaseOrderDetailId;


    @Column(name = "purchase_order_id", insertable = true,updatable = false)
    private Integer purchaseOrderId;

    @Column(name = "product_size")
    private String productSize;

    @Column(name = "karatage_id")
    private Integer karatageId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "karatage_id", insertable = false, updatable = false, nullable = true)
    private KaratageEntity karatage;

    @Column(name = "description")
    private String description;

    @Column(name = "wax_wt")
    private Double waxWt;

    @Column(name = "metal_wt")
    private Double metalWt;

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
