package com.tekfilo.jewellery.product.entity;

import com.tekfilo.jewellery.configmaster.entity.CollectionEntity;
import com.tekfilo.jewellery.configmaster.entity.ProductTypeEntity;
import com.tekfilo.jewellery.master.CustomerEntity;
import com.tekfilo.jewellery.master.EmployeeEntity;
import com.tekfilo.jewellery.master.SupplierEntity;
import com.tekfilo.jewellery.sku.entity.SkuEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_jewellery")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jew_generator")
    @SequenceGenerator(name = "jew_generator", sequenceName = "tbl_jewellery_seq", allocationSize = 1)
    @Column(name = "jew_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "jew_type", insertable = true, updatable = false)
    private String productType;

    @Column(name = "jew_no", insertable = true, updatable = false)
    private String productNo;

    @Column(name = "sku_id")
    private Integer skuId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "sku_id", referencedColumnName = "sku_id", insertable = false, updatable = false, nullable = false)
    private SkuEntity sku;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "design_id")
    private Integer designId;

    @Column(name = "design_no")
    private String designNo;

    @Column(name = "product_type_id")
    private Integer productTypeId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_type_id", insertable = false, updatable = false, nullable = true)
    private ProductTypeEntity jewType;


    @Column(name = "prod_description")
    private String prodDescription;


    @Column(name = "market_id", insertable = true, updatable = false)
    private Integer marketId;

    @Column(name = "product_size")
    private String productSize;

    @Column(name = "customer_id")
    private Integer customerId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false, nullable = true)
    private CustomerEntity customer;

    @Column(name = "salesman_id")
    private Integer salesmanId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salesman_id", insertable = false, updatable = false, nullable = true)
    private EmployeeEntity salesman;

    @Column(name = "collection_id")
    private Integer collectionId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collection_id", insertable = false, updatable = false, nullable = true)
    private CollectionEntity collection;


    @Column(name = "supplier_id")
    private Integer supplierId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false, nullable = true)
    private SupplierEntity supplier;

    @Column(name = "description")
    private String description;


    @Column(name = "image_url")
    private String picturePath;

    @Column(name = "metal1")
    private String metal1;

    @Column(name = "metal2")
    private String metal2;

    @Column(name = "metal3")
    private String metal3;

    @Column(name = "metal_weight")
    private Double metalWeight;

    @Column(name = "currency")
    private String currency;

    @Column(name = "labour_amount")
    private Double labourAmount;

    @Column(name = "stamping_details")
    private String stampingDetails;


    @Column(name = "uom", insertable = true, updatable = false)
    private String unit;

    @Column(name = "cost_price ")
    private Double costPrice;

    @Column(name = "sales_price ")
    private Double salesPrice;


    @Column(name = "remarks")
    private String remarks;
    @Column(name = "sort_seq")
    private Integer sequence;
    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "prod_category")
    private String prodCategory;

    @Column(name = "prod_remarks")
    private String prodRemarks;

    @Column(name = "video1_path")
    private String video1Path;


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
