package com.tekfilo.inventory.settlement.paymentreceived.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PendingInvoiceDto {

    private Integer id;
    private Integer paymentId;
    private Integer invId;
    private String invType;
    private String invoiceTypeNo;
    private String invoiceDate;
    private String invoiceDueDate;
    private String referenceNo;
    private Integer customerId;
    private Integer supplierId;
    private String customerName;
    private String currency;
    private Double exchangeRate;
    private String paymentTypeName;
    private BigDecimal totalInvoiceAmount;
    private BigDecimal totalPaidAmount;
    private BigDecimal totalReceivedAmount;
    private BigDecimal dueAmount;
    private BigDecimal receivedAmount;
    private BigDecimal receiveNow;
    private BigDecimal paidAmount;
    private BigDecimal paidNow;
}
