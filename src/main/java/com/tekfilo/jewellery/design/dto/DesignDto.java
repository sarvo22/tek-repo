package com.tekfilo.jewellery.design.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class DesignDto {

    private Integer id;
    private String designNo;
    private Integer productTypeId;
    private Integer marketId;
    private String productSize;
    private Integer customerId;
    private Integer designerId;
    private Integer collectionId;
    private Integer factoryId;
    private String description;
    private String imageUrl;
    private Double metalWeight;
    private String labourCurrency;
    private Double exchangeRate;
    private Integer sequence;
    private Integer isLocked;
    private Integer metalId;
    private String stampingRemarks;
    private String prodRemarks;
    private String category;
}
