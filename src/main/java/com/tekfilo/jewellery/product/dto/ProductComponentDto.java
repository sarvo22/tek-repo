package com.tekfilo.jewellery.product.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductComponentDto {
    private Integer id;
    private Integer jewId;
    private Integer lotId;
    private Integer commodityId;
    private Integer shapeId;
    private Integer cutId;
    private Integer colorId;
    private Integer clarityId;
    private String sieveSize;
    private String mmSize;
    private Double consumedQty1;
    private Double consumedQty2;
    private Double brokenQty1;
    private Double brokenQty2;
    private Double lossQty1;
    private Double lossQty2;
    private Double originalBrokenQty1;
    private Double originalBrokenQty2;
    private Double originalLossQty1;
    private Double originalLossQty2;
    private Double totalQty1;
    private Double totalQty2;
    private Integer settingTypeId;
    private Integer isCenterStone;
    private String currency;
    private Double exchangeRate;
    private Double ppRate;
}
