package com.tekfilo.jewellery.sku.entity;

import com.tekfilo.jewellery.configmaster.entity.CollectionEntity;
import com.tekfilo.jewellery.configmaster.entity.MarketEntity;
import com.tekfilo.jewellery.configmaster.entity.ProductTypeEntity;
import com.tekfilo.jewellery.design.entity.DesignEntity;
import com.tekfilo.jewellery.master.CustomerEntity;
import com.tekfilo.jewellery.master.EmployeeEntity;
import com.tekfilo.jewellery.master.FactoryEntity;
import com.tekfilo.jewellery.master.LotEntity;
import lombok.Data;
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
@Table(name = "tbl_sku")
public class SkuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sku_generator")
    @SequenceGenerator(name = "sku_generator", sequenceName = "tbl_sku_seq", allocationSize = 1)
    @Column(name = "sku_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "product_type_id")
    private Integer productTypeId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_type_id", insertable = false, updatable = false, nullable = true)
    private ProductTypeEntity productType;

    @Column(name = "market_id")
    private Integer marketId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "market_id", insertable = false, updatable = false, nullable = true)
    private MarketEntity market;


    @Column(name = "product_size")
    private String productSize;

    @Column(name = "customer_id")
    private Integer customerId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false, nullable = true)
    private CustomerEntity customer;

    @Column(name = "designer_id")
    private Integer designerId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "designer_id", referencedColumnName = "employee_id", insertable = false, updatable = false, nullable = true)
    private EmployeeEntity employee;

    @Column(name = "collection_id")
    private Integer collectionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collection_id", insertable = false, updatable = false, nullable = true)
    private CollectionEntity collection;

    @Column(name = "factory_id")
    private Integer factoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "factory_id", insertable = false, updatable = false, nullable = true)
    private FactoryEntity factory;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "metal_weight")
    private Double metalWeight;

    @Column(name = "labour_currency")
    private String labourCurrency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "design_id")
    private Integer designId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "design_id", referencedColumnName = "design_id", insertable = false, updatable = false, nullable = true)
    private DesignEntity design;

    @Column(name = "metal_id")
    private Integer metalId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metal_id", insertable = false, updatable = false, nullable = true)
    private LotEntity metal;

    @Column(name = "stamping_remarks")
    private String stampingRemarks;

    @Column(name = "prod_remarks")
    private String prodRemarks;

    @Column(name = "category")
    private String category;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "sort_seq")
    private Integer sequence;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "created_by", insertable = true, updatable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_dt", insertable = true, updatable = false)
    private Timestamp createdDate;

    @Column(name = "modified_by", insertable = false, updatable = true)
    private Integer modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_dt", insertable = false, updatable = true)
    private Timestamp modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
