package com.tekfilo.inventory.order.purchase.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class PurchaseOrderDetailDto {

    private Integer id;
    private Integer purchaseOrderId;
    private Integer productId;
    private Integer commodityId;
    private String measurement;

    private Integer shapeId;
    private Integer cutId;
    private Integer colorId;
    private Integer clarityId;

    private Integer isCertified;
    private Integer isTreated;
    private Double qty1;
    private Double qty2;
    private String currencyCode;
    private Double exchangeRate;
    private BigDecimal ppRate;
    private BigDecimal ppAmount;
    private String cancelHoldStatus;
    private Double invoicedQty;
    private Integer operateBy;

}
