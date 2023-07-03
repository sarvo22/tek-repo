package com.tekfilo.account.transaction.cashpaymentreceipt.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Data
@Getter
@Setter
public class CashPaymentReceiptMainDto {
    private Integer id;
    private String invoiceType;
    private String invoiceNo;
    private Date invoiceDate;
    private Date invoiceDueDate;
    private String referenceNo;
    private String currency;
    private Double exchangeRate;
    private Integer cashAccountId;
    private String note;
    private String accountingStatus;
    private String paymentStatus;
    private Double totalDebitAmount;
    private Double totalCreditAmount;
    private Double settledAmount;
    private String partyType;
    private Integer partyId;
}
