package com.tekfilo.inventory.settlement.paymentpaid.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PendingPaidInvoiceDto {

    private Integer id;
    private Integer paymentId;
    private Integer invId;
    private String invoiceType;
    private String invType;
    private String invoiceNo;
    private String invoiceTypeNo;
    private String invoiceDate;
    private String invoiceDueDate;
    private String referenceNo;
    private Integer supplierId;
    private String supplierName;
    private String currency;
    private Double exchangeRate;
    private String paymentTypeName;
    private BigDecimal totalInvoiceAmount;
    private BigDecimal totalPaidAmount;
    private BigDecimal dueAmount;
    private BigDecimal paidAmount;
}
