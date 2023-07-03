package com.tekfilo.inventory.item.invoice.common;

import com.tekfilo.inventory.base.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ItemInvoiceMainDto extends BaseDto {

    private Integer id;
    private String invoiceType;
    private String invoiceNo;
    private String referenceNo;
    private Date invoiceDate;
    private Date invoiceDueDate;
    private Double invoiceDueMonths;
    private Double invoiceDueDays;
    private String currency;
    private Double exchangeRate;
    private Integer customerId;
    private Integer supplierId;
    private Integer headSalesmanId;
    private Integer salesmanId;
    private Integer headBuyerId;
    private Integer buyerId;
    private Integer paymentTypeId;
    private Integer shipmentTypeId;
    private Integer carrierId;
    private String shipmentCompanyName;
    private String trackingNo;
    private String trackingStatus;
    private String invoiceStatus;
    private String paymentStatus;
    private String cancelHoldStatus;
    private String documentUrl;
    private BigDecimal totalInvoiceAmount;
    private BigDecimal totalPaidAmount;
    private Integer billingAddressId;
    private Integer shippingAddressId;
    private String supplierNotes;
    private String customerNotes;
    private String termsAndCondition;
    private String accountingStatus;
    private Integer orderId;
}
