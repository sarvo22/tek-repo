package com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice.entity;

import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceMainEntity;
import com.tekfilo.jewellery.master.BinEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_jwe_memo_sales_return_invoice_det")
public class MemoSalesReturnInvoiceDetailEntity {

    @Transient
    MemoSalesInvoiceMainEntity salesInvoiceMain;

    @Transient
    MemoSalesReturnInvoiceMainEntity memoSalesReturnInvoiceMain;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memosalesretinvoicedet_generator")
    @SequenceGenerator(name = "memosalesretinvoicedet_generator", sequenceName = "tbl_jew_memo_sales_return_invoice_det_seq", allocationSize = 1)
    @Column(name = "inv_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "inv_id")
    private Integer invId;

    @Column(name = "ref_inv_type")
    private String referenceInvoiceType;


    @Column(name = "ref_inv_id")
    private Integer referenceInvoiceId;

    @Column(name = "jew_id")
    private Integer productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "jew_id", referencedColumnName = "jew_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity product;

    @Column(name = "bin_id")
    private Integer binId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", insertable = false, updatable = false)
    private BinEntity bin;

    @Column(name = "prod_description")
    private String productDescription;

    @Column(name = "inv_qty")
    private BigDecimal invQty;

    @Column(name = "conf_qty")
    private BigDecimal confQty;

    @Column(name = "ret_qty")
    private BigDecimal retQty;

    @Column(name = "uom")
    private String uom;

    @Column(name = "input_rate")
    private BigDecimal inputRate;

    @Column(name = "input_amt")
    private BigDecimal inputAmount;

    @Column(name = "conf_input_rate")
    private BigDecimal confInputRate;

    @Column(name = "conf_input_amt")
    private BigDecimal confInputAmount;


    @Column(name = "ret_input_rate")
    private BigDecimal retInputRate;

    @Column(name = "ret_input_amt")
    private BigDecimal retInputAmount;

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
