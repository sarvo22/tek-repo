package com.tekfilo.jewellery.product.dto;

import com.tekfilo.jewellery.base.BaseDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class ProductDto extends BaseDto implements Serializable {
    private Integer id;
    private String productType;
    private String productNo;
    private Integer skuId;
    private String skuNo;
    private String designId;
    private String designNo;
    private Integer productTypeId;
    private String prodDescription;
    private Integer marketId;
    private String productSize;
    private Integer customerId;
    private Integer salesmanId;
    private Integer collectionId;
    private Integer supplierId;
    private String description;
    private String picturePath;
    private String metal1;
    private String metal2;
    private String metal3;
    private Double metalWeight;
    private String currency;
    private Double labourAmount;
    private String stampingDetails;
    private String unit;
    private Double costPrice;
    private Double salesPrice;
    private String remarks;
    private Integer isLocked;
    private Integer companyId;
    private Integer isDeleted;
    private Double exchangeRate;
    private String prodCategory;
    private String prodRemarks;
    private String video1Path;
}
