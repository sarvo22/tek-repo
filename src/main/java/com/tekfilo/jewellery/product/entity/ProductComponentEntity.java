package com.tekfilo.jewellery.product.entity;

import com.tekfilo.jewellery.master.*;
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
@Table(name = "tbl_jewellery_component")
public class ProductComponentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jew_generator")
    @SequenceGenerator(name = "jew_generator", sequenceName = "tbl_jewellery_seq", allocationSize = 1)
    @Column(name = "jew_component_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "jew_id")
    private Integer jewId;

    @Column(name = "product_id")
    private Integer lotId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", insertable = false, updatable = false, nullable = true)
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

    @Column(name = "clarity_id")
    private Integer clarityId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clarity_id", insertable = false, updatable = false, nullable = true)
    private ClarityEntity clarity;


    @Column(name = "sieve_size")
    private String sieveSize;

    @Column(name = "mm_size")
    private String mmSize;

    @Column(name = "consumed_qty1")
    private Double consumedQty1;

    @Column(name = "consumed_qty2")
    private Double consumedQty2;

    @Column(name = "broken_qty1")
    private Double brokenQty1;

    @Column(name = "broken_qty2")
    private Double brokenQty2;

    @Column(name = "loss_qty1")
    private Double lossQty1;

    @Column(name = "loss_qty2")
    private Double lossQty2;

    @Column(name = "original_broken_qty1")
    private Double originalBrokenQty1;

    @Column(name = "original_broken_qty2")
    private Double originalBrokenQty2;

    @Column(name = "original_loss_qty1")
    private Double originalLossQty1;

    @Column(name = "original_loss_qty2")
    private Double originalLossQty2;

    @Column(name = "total_qty1")
    private Double totalQty1;

    @Column(name = "total_qty2")
    private Double totalQty2;

    @Column(name = "setting_type_id")
    private Integer settingTypeId;

    @Column(name = "is_center_stone")
    private Integer isCenterStone;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "pp_rate")
    private Double ppRate;

    @Column(name = "company_id")
    private Integer companyId;


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
