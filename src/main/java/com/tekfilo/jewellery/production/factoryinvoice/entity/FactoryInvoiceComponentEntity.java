package com.tekfilo.jewellery.production.factoryinvoice.entity;

import com.tekfilo.jewellery.master.LotEntity;
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
@Table(name = "tbl_factory_invoice_component")
public class FactoryInvoiceComponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "finvcomponent_generator")
    @SequenceGenerator(name = "finvcomponent_generator", sequenceName = "tbl_factory_invoice_component_seq", allocationSize = 1)
    @Column(name = "component_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_det_id", insertable = true, updatable = false, nullable = false)
    private Integer invoiceDetailId;

    @Column(name = "inv_id", insertable = true, updatable = false, nullable = false)
    private Integer invoiceId;

    @Column(name = "jew_id", insertable = true, updatable = false, nullable = false)
    private Integer jewId;

    @Column(name = "lot_id", insertable = true, updatable = false, nullable = false)
    private Integer lotId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lot_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = true)
    private LotEntity lot;

    @Column(name = "bin_id", insertable = true, updatable = false, nullable = false)
    private Integer binId;

    @Column(name = "consumed_qty1", nullable = false)
    private Double consumedQty1;

    @Column(name = "consumed_qty2", nullable = false)
    private Double consumedQty2;

    @Column(name = "broken_qty1", nullable = false)
    private Double brokenQty1;

    @Column(name = "broken_qty2", nullable = false)
    private Double brokenQty2;

    @Column(name = "loss_qty1", nullable = false)
    private Double lossQty1;

    @Column(name = "loss_qty2", nullable = false)
    private Double lossQty2;

    @Column(name = "original_broken_qty1", nullable = false)
    private Double originalBrokenQty1;

    @Column(name = "original_broken_qty2", nullable = false)
    private Double originalBrokenQty2;

    @Column(name = "original_loss_qty1", nullable = false)
    private Double originalLossQty1;

    @Column(name = "original_loss_qty2", nullable = false)
    private Double originalLossQty2;

    @Column(name = "return_qty1", nullable = false)
    private Double returnQty1;

    @Column(name = "return_qty2", nullable = false)
    private Double returnQty2;

    @Column(name = "total_qty1", nullable = false)
    private Double totalQty1;

    @Column(name = "total_qty2", nullable = false)
    private Double totalQty2;

    @Column(name = "cogs_amount", nullable = false)
    private Double cogsAmount;

    @Column(name = "is_center_stone", nullable = false)
    private Integer isCenterStone;

    @Column(name = "uom1", nullable = false)
    private String uom1;

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
