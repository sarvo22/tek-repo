package com.tekfilo.jewellery.production.studinvoice.entity;

import com.tekfilo.jewellery.master.BinEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_stud_invoice_det")
public class StudInvoiceDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studinvoicedetmain_generator")
    @SequenceGenerator(name = "studinvoicedetmain_generator", sequenceName = "tbl_stud_invoice_det_seq", allocationSize = 1)
    @Column(name = "inv_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_id", insertable = true, updatable = false, nullable = false)
    private Integer invoiceId;

    @Column(name = "source_jew_id", insertable = true, updatable = false, nullable = false)
    private Integer sourceJewId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_jew_id", referencedColumnName = "jew_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity sourceProduct;

    @Column(name = "dest_jew_id", insertable = true, updatable = false, nullable = false)
    private Integer destJewId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dest_jew_id", referencedColumnName = "jew_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity destProduct;


    @Column(name = "source_bin_id", insertable = true, updatable = false, nullable = false)
    private Integer sourceBinId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_bin_id", referencedColumnName = "bin_id", insertable = false, updatable = false, nullable = true)
    private BinEntity sourceBin;

    @Column(name = "dest_bin_id", insertable = true, updatable = false, nullable = false)
    private Integer destBinId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dest_bin_id", referencedColumnName = "bin_id", insertable = false, updatable = false, nullable = true)
    private BinEntity destBin;


    @Column(name = "inv_qty", insertable = true, updatable = true, nullable = false)
    private Double invQty;

    @Column(name = "labour_amount", insertable = true, updatable = true, nullable = false)
    private BigDecimal labourAmount;

    @Column(name = "gold_loss_pct", insertable = true, updatable = true, nullable = false)
    private Double goldLossPct;

    @Column(name = "platinum_loss_pct", insertable = true, updatable = true, nullable = false)
    private Double silverLossPct;

    @Column(name = "silver_loss_pct", insertable = true, updatable = true, nullable = false)
    private Double platinumLossPct;

    @Column(name = "admin_pct", insertable = true, updatable = true, nullable = false)
    private Double adminPct;

    @Column(name = "gross_wt", insertable = true, updatable = true, nullable = false)
    private BigDecimal grossWt;

    @Column(name = "total_cogs_amount", insertable = true, updatable = true, nullable = false)
    private BigDecimal totalCogsAmount;

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
