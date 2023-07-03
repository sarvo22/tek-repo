package com.tekfilo.jewellery.sku.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuFindingDto implements Serializable {
    private Integer id;
    private Integer skuId;
    private String findingName;
    private String description;
    private Double qty;
}
