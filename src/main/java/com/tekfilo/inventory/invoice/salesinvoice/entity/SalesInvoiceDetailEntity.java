package com.tekfilo.inventory.invoice.salesinvoice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tekfilo.inventory.bin.BinEntity;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.stock.StockEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tbl_sales_invoice_det")
public class SalesInvoiceDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salesinvoicedet_generator")
    @SequenceGenerator(name = "salesinvoicedet_generator", sequenceName = "tbl_sales_invoice_det_seq", allocationSize = 1)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inv_id", referencedColumnName = "inv_id", insertable = false, updatable = false, nullable = false)
    private SalesInvoiceMainEntity salesInvoiceMain;

    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity product;

    @Column(name = "prod_description")
    private String productDescription;

    @Column(name = "bin_id")
    private Integer binId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bin_id", insertable = false, updatable = false)
    private BinEntity bin;

    @Column(name = "inv_qty1")
    private BigDecimal invQty1;

    @Column(name = "inv_qty2")
    private BigDecimal invQty2;

    @Column(name = "uom1")
    private String uom1;

    @Column(name = "uom2")
    private String uom2;

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

    @Column(name = "conf_qty1")
    private BigDecimal confQty1;

    @Column(name = "conf_qty2")
    private BigDecimal confQty2;

    @Column(name = "ret_qty1")
    private BigDecimal retQty1;

    @Column(name = "ret_qty2")
    private BigDecimal retQty2;

    @Column(name = "conf_input_rate")
    private BigDecimal confInputRate;

    @Column(name = "conf_input_amt")
    private BigDecimal confInputAmount;


    @Column(name = "ret_input_rate")
    private BigDecimal retInputRate;

    @Column(name = "ret_input_amt")
    private BigDecimal retInputAmount;

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

    @Transient
    private StockEntity stock;

}
