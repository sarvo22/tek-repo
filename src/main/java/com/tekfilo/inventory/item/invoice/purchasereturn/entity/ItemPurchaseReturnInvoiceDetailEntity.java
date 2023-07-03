package com.tekfilo.inventory.item.invoice.purchasereturn.entity;

import com.tekfilo.inventory.bin.BinEntity;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceMainEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_item_purchase_return_invoice_det")
public class ItemPurchaseReturnInvoiceDetailEntity {

    @Transient
    ItemPurchaseInvoiceMainEntity purchaseInvoiceMain;

    @Transient
    ItemPurchaseReturnInvoiceMainEntity purchaseReturnInvoiceMain;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itempurchaseretinvoicedet_generator")
    @SequenceGenerator(name = "itempurchaseretinvoicedet_generator", sequenceName = "tbl_item_purchase_return_invoice_det_seq", allocationSize = 1)
    @Column(name = "inv_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;
    @Column(name = "inv_id")
    private Integer invId;
    @Column(name = "ref_inv_type")
    private String referenceInvoiceType;
    @Column(name = "ref_inv_id")
    private Integer referenceInvoiceId;
    @Column(name = "ref_inv_det_id")
    private Integer referenceInvoiceDetailId;
    @Column(name = "item_id")
    private Integer productId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", insertable = false, updatable = false, nullable = true)
    private ItemEntity product;
    @Column(name = "item_description")
    private String productDescription;
    @Column(name = "bin_id")
    private Integer binId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", insertable = false, updatable = false)
    private BinEntity bin;
    @Column(name = "inv_qty")
    private BigDecimal invQty;
    @Column(name = "uom")
    private String uom;
    @Column(name = "input_rate")
    private BigDecimal inputRate;
    @Column(name = "input_amt")
    private BigDecimal inputAmount;
    @Column(name = "discount_type")
    private String discountType;
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    @Column(name = "cost_price")
    private BigDecimal costPrice;
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
