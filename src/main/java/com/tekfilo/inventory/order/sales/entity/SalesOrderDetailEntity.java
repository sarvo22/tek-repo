package com.tekfilo.inventory.order.sales.entity;

import com.tekfilo.inventory.clarity.ClarityEntity;
import com.tekfilo.inventory.color.ColorEntity;
import com.tekfilo.inventory.commodity.CommodityEntity;
import com.tekfilo.inventory.cut.CutEntity;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.shape.ShapeEntity;
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
@Table(name = "tbl_sales_order_det")
public class SalesOrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salesorderdet_generator")
    @SequenceGenerator(name = "salesorderdet_generator", sequenceName = "tbl_sales_order_det_seq", allocationSize = 1)
    @Column(name = "sales_order_det_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "sales_order_id", insertable = true, updatable = false)
    private Integer salesOrderId;

    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false, nullable = true)
    private ProductEntity product;

    @Column(name = "commodity_id")
    private Integer commodityId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "commodity_id", referencedColumnName = "commodity_id", insertable = false, updatable = false, nullable = true)
    private CommodityEntity commodity;

    @Column(name = "shape_id")
    private Integer shapeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "shape_id", referencedColumnName = "shape_id", insertable = false, updatable = false, nullable = true)
    private ShapeEntity shape;

    @Column(name = "cut_id")
    private Integer cutId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "cut_id", referencedColumnName = "cut_id", insertable = false, updatable = false, nullable = true)
    private CutEntity cut;

    @Column(name = "color_id")
    private Integer colorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "color_id", referencedColumnName = "color_id", insertable = false, updatable = false, nullable = true)
    private ColorEntity color;

    @Column(name = "clarity_id")
    private Integer clarityId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "clarity_id", referencedColumnName = "clarity_id", insertable = false, updatable = false, nullable = true)
    private ClarityEntity clarity;

    @Column(name = "measurement_lxb_h")
    private String measurement;

    @Column(name = "certified")
    private Integer isCertified;

    @Column(name = "treated")
    private Integer isTreated;

    @Column(name = "qty1")
    private Double qty1;

    @Column(name = "qty2")
    private Double qty2;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "sp_rate")
    private BigDecimal spRate;

    @Column(name = "sp_amt")
    private BigDecimal spAmount;

    @Column(name = "cancel_hold_status")
    private String cancelHoldStatus;

    @Column(name = "invoiced_qty")
    private Double invoicedQty;

    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "discount_value")
    private Double discountValue;

    @Column(name = "final_sp_amt")
    private BigDecimal finalSpAmount;

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
