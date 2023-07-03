package com.tekfilo.jewellery.order.purchase.entity;

import com.tekfilo.jewellery.configmaster.entity.ProductTypeEntity;
import com.tekfilo.jewellery.configmaster.entity.SettingTypeEntity;
import com.tekfilo.jewellery.master.*;
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

@Getter
@Setter
@Entity
@Table(name = "tbl_jew_purchase_order_component")
public class PurchaseOrderComponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchaseordercomponent_generator")
    @SequenceGenerator(name = "purchaseordercomponent_generator", sequenceName = "tbl_jew_purchase_order_component_seq", allocationSize = 1)
    @Column(name = "component_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "purchase_order_det_id", insertable = true,updatable = false)
    private Integer purchaseOrderDetailId;


    @Column(name = "purchase_order_id", insertable = true,updatable = false)
    private Integer purchaseOrderId;

    @Column(name = "lot_id")
    private Integer productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lot_id", insertable = false, updatable = false, nullable = true)
    private LotEntity lot;

    @Column(name = "commodity_id")
    private Integer commodityId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commodity_id", insertable = false, updatable = false, nullable = true)
    private CommodityEntity commodity;

    @Column(name = "shape_id")
    private Integer shapeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shape_id", insertable = false, updatable = false, nullable = true)
    private ShapeEntity shape;

    @Column(name = "color_id")
    private Integer colorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "color_id", insertable = false, updatable = false, nullable = true)
    private ColorEntity color;

    @Column(name = "cut_id")
    private Integer cutId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cut_id", insertable = false, updatable = false, nullable = true)
    private CutEntity cut;

    @Column(name = "setting_type_id")
    private Integer settingTypeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "setting_type_id", insertable = false, updatable = false, nullable = true)
    private SettingTypeEntity setting;

    @Column(name = "sieve_size")
    private String sieveSize;

    @Column(name = "mm_size")
    private String mmSize;

    @Column(name = "qty1")
    private Double qty1;

    @Column(name = "qty2")
    private Double qty2;

    @Column(name = "total_wt")
    private Double totalWt;

    @Column(name = "is_center_stone")
    private Integer isCenterStone;



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
