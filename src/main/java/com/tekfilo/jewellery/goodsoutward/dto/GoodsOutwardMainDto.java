package com.tekfilo.jewellery.goodsoutward.dto;

import com.tekfilo.jewellery.base.BaseDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter
@Setter
public class GoodsOutwardMainDto extends BaseDto {

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
    private String partyType;
    private Integer partyId;
    private Integer headEmployeeId;
    private Integer employeeId;
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
    private String termsAndCondition;
    private String accountingStatus;
}
