package com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_jew_purchase_return_invoice_chgs")
public class PurchaseReturnInvoiceChargesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchaseretinvoicechgs_generator")
    @SequenceGenerator(name = "purchaseretinvoicechgs_generator", sequenceName = "tbl_jew_purchase_return_invoice_chgs_seq", allocationSize = 1)
    @Column(name = "charge_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_id")
    private Integer invId;

    @Column(name = "charge_name")
    private String chargeName;

    @Column(name = "plus_minus_flag")
    private Integer plusMinusFlag;

    @Column(name = "input_pct_amt_type")
    private String inputPctAmountType;

    @Column(name = "input_pct_amt_value")
    private BigDecimal inputPctAmountValue;

    @Column(name = "input_amt")
    private BigDecimal inputAmount;

    @Column(name = "is_supplier_payable")
    private Integer isSupplierPayable;

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
