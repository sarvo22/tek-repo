package com.tekfilo.inventory.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Data
public class ItemDto {

    private Integer id;
    private Integer itemCategoryId;
    private String itemType;
    private String itemName;
    private String skuNo;
    private String unit;
    private Integer isReturn;
    private String dimension;
    private String dimensionUom;
    private Double weight;
    private String weightUom;
    private Integer manufacturerId;
    private Integer brandId;
    private String barcodeNo;
    private String qrcodeNo;
    private String imageUrl;
    private Double qty;
    private Double salesPrice;
    private Integer salesAccountId;
    private String salesDescription;
    private Double costPrice;
    private Double purchaseAccountId;
    private String purchaseDescription;
    private Integer inventoryAccountId;
    private Double reorderQty;
    private Integer supplierId;
    private String description;
}
