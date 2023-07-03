package com.tekfilo.jewellery.production.factoryinvoice.entity;

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
@Table(name = "tbl_factory_invoice_det")
public class FactoryInvoiceDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factoryinvoicedetmain_generator")
    @SequenceGenerator(name = "factoryinvoicedetmain_generator", sequenceName = "tbl_factory_invoice_det_seq", allocationSize = 1)
    @Column(name = "inv_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_id", insertable = true, updatable = false, nullable = false)
    private Integer invoiceId;

    @Column(name = "jew_id", insertable = true, updatable = false, nullable = false)
    private Integer jewId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jew_id", referencedColumnName = "jew_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity product;

    @Column(name = "bin_id", insertable = true, updatable = false, nullable = false)
    private Integer binId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", referencedColumnName = "bin_id", insertable = false, updatable = false, nullable = true)
    private BinEntity bin;

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
