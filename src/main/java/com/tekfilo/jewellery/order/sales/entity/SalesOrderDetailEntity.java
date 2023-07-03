package com.tekfilo.jewellery.order.sales.entity;

import com.tekfilo.jewellery.configmaster.entity.ProductTypeEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_jew_sales_order_det")
public class SalesOrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jewsalesorderdet_generator")
    @SequenceGenerator(name = "jewsalesorderdet_generator", sequenceName = "tbl_jew_sales_order_det_seq", allocationSize = 1)
    @Column(name = "sales_order_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "sales_order_id")
    private Integer salesOrderId;

    @Column(name = "sku_id")
    private Integer skuId;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "product_type_id")
    private Integer productTypeId;


    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "product_type_id", referencedColumnName = "product_type_id", insertable = false, updatable = false, nullable = true)
    private ProductTypeEntity productType;

    @Column(name = "description")
    private String description;

    @Column(name = "qty")
    private Double qty;

    @Column(name = "metal")
    private String metal;

    @Column(name = "stamping_details")
    private String stampingDetails;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "pp_rate")
    private BigDecimal ppRate;

    @Column(name = "pp_amt")
    private BigDecimal ppAmount;

    @Column(name = "cancel_hold_status")
    private String cancelHoldStatus;

    @Column(name = "invoiced_qty")
    private Double invoicedQty;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "system_remarks")
    private String systemRemarks;

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
