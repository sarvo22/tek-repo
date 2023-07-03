package com.tekfilo.jewellery.sku.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuMouldPartDto implements Serializable {
    private Integer id;
    private Integer skuId;
    private String partName;
    private String partNo;
    private String description;
    private Double pieces;
}
