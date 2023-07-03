package com.tekfilo.inventory.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_generator")
    @SequenceGenerator(name = "item_generator", sequenceName = "tbl_item_seq", allocationSize = 1)
    @Column(name = "item_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "category_id")
    private Integer itemCategoryId;

    @Column(name = "item_type")
    private String itemType;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "description")
    private String description;

    @Column(name = "unit")
    private String unit;

    @Column(name = "is_return")
    private Integer isReturn;

    @Column(name = "dimension")
    private String dimension;

    @Column(name = "dimension_uom")
    private String dimensionUom;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "weight_uom")
    private String weightUom;

    @Column(name = "manufacturer_id")
    private Integer manufacturerId;

    @Column(name = "brand")
    private Integer brandId;

    @Column(name = "barcode_no")
    private String barcodeNo;

    @Column(name = "qrcode_no")
    private String qrcodeNo;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "qty")
    private Double qty;

    @Column(name = "sales_price")
    private Double salesPrice;

    @Column(name = "sales_account_id")
    private Integer salesAccountId;

    @Column(name = "sales_description")
    private String salesDescription;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(name = "purchase_account_id")
    private Double purchaseAccountId;

    @Column(name = "purchase_description")
    private String purchaseDescription;

    @Column(name = "inventory_account_id")
    private Integer inventoryAccountId;

    @Column(name = "reorder_qty")
    private Double reorderQty;

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "company_id")
    private Integer companyId;

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
