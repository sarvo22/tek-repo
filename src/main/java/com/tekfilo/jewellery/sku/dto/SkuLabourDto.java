package com.tekfilo.jewellery.sku.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class SkuLabourDto implements Serializable {
    private Integer id;
    private Integer skuId;
    private Integer processId;
    private String description;
    private Double qty;
    private Double rate;
    private Double amount;
}
