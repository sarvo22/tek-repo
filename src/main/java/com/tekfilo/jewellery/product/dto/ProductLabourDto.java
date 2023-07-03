package com.tekfilo.jewellery.product.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductLabourDto implements Serializable {
    private Integer id;
    private Integer jewId;
    private Integer processId;
    private String description;
    private Double qty;
    private Double rate;
    private Double amount;
}
