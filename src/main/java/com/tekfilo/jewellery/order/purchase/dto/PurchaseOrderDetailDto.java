package com.tekfilo.jewellery.order.purchase.dto;

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
    private Integer skuId;
    private String skuNo;
    private Integer productTypeId;
    private String productSize;
    private String description;
    private Double qty;
    private String metal;
    private Double metalWt;
    private String prodRemarks;
    private Integer jewId;
    private String orderType;
    private String stampingDetails;
    private String currencyCode;
    private Double exchangeRate;
    private BigDecimal ppRate;
    private BigDecimal ppAmount;
    private String cancelHoldStatus;
    private Double invoicedQty;
    private String remarks;
    private String systemRemarks;
    private Integer sequence;
    private Integer isLocked;

}
