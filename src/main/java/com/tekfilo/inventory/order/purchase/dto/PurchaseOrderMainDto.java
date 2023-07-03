package com.tekfilo.inventory.order.purchase.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Data
@Getter
@Setter
public class PurchaseOrderMainDto {

    private Integer id;
    private String purchaseOrderNo;
    private String purchaseOrderType;
    private Date purchaseOrderDate;
    private Date deliveryDate;

    private String referenceNo;
    private Integer supplierId;

    private Integer billingAddressId;
    private Integer shippingAddressId;

    private Integer buyerId;
    private Integer paymentTermId;
    private String poStatus;
    private String invoiceStatus;
    private String document;
    private String remarks;
    private Integer operateBy;
    private String currencyCode;
    private Double exchangeRate;
}
