package com.tekfilo.jewellery.issuereceive.entity;

import com.tekfilo.jewellery.master.BinEntity;
import com.tekfilo.jewellery.master.LotEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_bagging_issue_respective_det")
public class BaggingIssueRespectiveDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bagging_issue_det_generator")
    @SequenceGenerator(name = "bagging_issue_det_generator", sequenceName = "tbl_bagging_issue_respective_det_seq", allocationSize = 1)
    @Column(name = "inv_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_id")
    private Integer invId;

    @Column(name = "jew_id")
    private Integer jewId;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jew_id", referencedColumnName = "jew_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity jew;

    @Column(name = "lot_id")
    private Integer lotId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lot_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = true)
    private LotEntity lot;

    @Column(name = "bin_id")
    private Integer binId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", referencedColumnName = "bin_id", insertable = false, updatable = false, nullable = true)
    private BinEntity bin;

    @Column(name = "ref_inv_id")
    private Integer refInvoiceId;

    @Column(name = "ref_inv_det_id")
    private Integer refInvoiceDetailId;

    @Column(name = "ref_inv_type")
    private String refInvoiceType;

    @Column(name = "description")
    private String description;


    @Column(name = "inv_qty")
    private Double invQty;

    @Column(name = "uom")
    private String uom;

    @Column(name = "inv_qty1")
    private Double invQty1;

    @Column(name = "uom1")
    private String uom1;

    @Column(name = "inv_qty2")
    private Double invQty2;

    @Column(name = "uom2")
    private String uom2;


    @Column(name = "remarks")
    private String remarks;

    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "created_by", insertable = true, updatable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDt;

    @Column(name = "modified_by", insertable = false, updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDt;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
