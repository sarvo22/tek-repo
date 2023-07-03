package com.tekfilo.inventory.order.sales.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesOrderDetailDto {

    private Integer id;
    private Integer salesOrderId;
    private Integer productId;
    private Integer commodityId;

    private Integer shapeId;
    private Integer cutId;
    private Integer colorId;
    private Integer clarityId;

    private String measurement;
    private Integer isCertified;
    private Integer isTreated;
    private Double qty1;
    private Double qty2;
    private String currencyCode;
    private Double exchangeRate;
    private BigDecimal spRate;
    private BigDecimal spAmount;
    private String discountType;
    private Double discountValue;
    private BigDecimal finalSpAmount;
    private String cancelHoldStatus;
    private Double invoicedQty;
    private Integer operateBy;

}
