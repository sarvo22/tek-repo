package com.tekfilo.inventory.order.sales.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Data
@Getter
@Setter
public class SalesOrderMainDto {

    private Integer id;
    private String salesOrderType;
    private String salesOrderNo;
    private Date salesOrderDate;
    private String referenceNo;
    private Integer customerId;
    private Date deliveryDate;
    private Integer billingAddressId;
    private Integer shippingAddressId;
    private Integer salesmanId;
    private Integer paymentTermId;
    private String soStatus;
    private String invoiceStatus;
    private String document;
    private String remarks;
    private Integer operateBy;
    private String currencyCode;
    private Double exchangeRate;
}
