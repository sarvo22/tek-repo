package com.tekfilo.inventory.product;

import com.tekfilo.inventory.clarity.ClarityEntity;
import com.tekfilo.inventory.color.ColorEntity;
import com.tekfilo.inventory.commodity.CommodityEntity;
import com.tekfilo.inventory.commoditygroup.CommodityGroupEntity;
import com.tekfilo.inventory.cut.CutEntity;
import com.tekfilo.inventory.karatage.KaratageEntity;
import com.tekfilo.inventory.shape.ShapeEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name = "product_generator", sequenceName = "tbl_product_seq", allocationSize = 1)
    @Column(name = "product_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "product_no", insertable = true, updatable = false)
    private String productNo;

    @Column(name = "description")
    private String description;

    @Column(name = "commodity_id", insertable = true, updatable = false)
    private Integer commodityId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commodity_id", insertable = false, updatable = false)
    private CommodityEntity commodity;

    @Column(name = "commodity_group_id", insertable = true, updatable = false)
    private Integer commodityGroupId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commodity_group_id", insertable = false, updatable = false)
    private CommodityGroupEntity commodityGroup;


    @Column(name = "sieve_size")
    private String sieveSize;

    @Column(name = "measurement_lxb_h")
    private String measurementLxbH;

    @Column(name = "measurement_l")
    private String measurementL;

    @Column(name = "measurement_b")
    private String measurementB;

    @Column(name = "measurement_h")
    private String measurementH;

    @Column(name = "shape_id")
    private Integer shapeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "shape_id", insertable = false, updatable = false)
    private ShapeEntity shape;


    @Column(name = "cut_id")
    private Integer cutId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "cut_id", insertable = false, updatable = false)
    private CutEntity cut;

    @Column(name = "color_id")
    private Integer colorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "color_id", insertable = false, updatable = false)
    private ColorEntity color;

    @Column(name = "clarity_id")
    private Integer clarityId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "clarity_id", insertable = false, updatable = false)
    private ClarityEntity clarity;


    @Column(name = "karatage_id")
    private Integer karatageId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "karatage_id", insertable = false, updatable = false)
    private KaratageEntity karatage;

    @Column(name = "carat_weight ")
    private Double caratWeight;

    @Column(name = "mm_size")
    private String mmSize;

    @Column(name = "lab_name")
    private String labName;

    @Column(name = "certificate_no")
    private String certificateNo;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "transparency")
    private String transparency;

    @Column(name = "origin")
    private String origin;

    @Column(name = "specific_gravity")
    private String specificGravity;

    @Column(name = "certificate_image_path")
    private String certificateImagePath;

    @Column(name = "certificate_url")
    private String certificateUrl;

    @Column(name = "polish")
    private String polish;

    @Column(name = "symmetry")
    private String symmetry;

    @Column(name = "fluorescence")
    private String fluorescence;

    @Column(name = "inscription_no")
    private String inscriptionNo;

    @Column(name = "table_description")
    private String tableDescription;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "rapaport_price")
    private Double rapaportPrice;

    @Column(name = "rapaport_date")
    private Date rapaportDate;

    @Column(name = "purity")
    private String purity;

    @Column(name = "uom1", insertable = true, updatable = false)
    private String unit1;

    @Column(name = "uom2", insertable = true, updatable = false)
    private String unit2;

    @Column(name = "comments")
    private String comments;

    @Column(name = "cost_price ")
    private Double costPrice;

    @Column(name = "sales_price ")
    private Double salesPrice;

    @Column(name = "picture1_path")
    private String picture1Path;

    @Column(name = "picture2_path")
    private String picture2Path;

    @Column(name = "picture3_path")
    private String picture3Path;

    @Column(name = "picture4_path")
    private String picture4Path;

    @Column(name = "video1_path")
    private String video1Path;

    @Column(name = "video2_path")
    private String video2Path;

    @Column(name = "stone_type")
    private String stoneType;

    @Column(name = "min_wt")
    private Double minWt;

    @Column(name = "max_wt")
    private Double maxWt;

    @Column(name = "a_price")
    private Double APrice;

    @Column(name = "b_price")
    private Double BPrice;

    @Column(name = "depth")
    private String depth;

    @Column(name = "remarks")
    private String remarks;
    @Column(name = "sort_seq")
    private Integer sequence;
    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "prod_category", insertable = true, updatable = false)
    private String prodCategory;

    @Column(name = "document_url")
    private String documentUrls;

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