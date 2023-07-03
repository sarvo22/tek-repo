package com.tekfilo.jewellery.sku.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuWaxDto implements Serializable {
    private Integer id;
    private Integer skuId;
    private String productSize;
    private Integer karatageId;
    private String description;
    private Double waxWt;
    private Double metalWt;
}
