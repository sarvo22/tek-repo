package com.tekfilo.account.transaction.bankpaymentreceipt.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class BankPaymentReceiptMainDto {
    private Integer id;
    private String invoiceType;
    private String invoiceNo;
    private Date invoiceDate;
    private Date invoiceDueDate;
    private String referenceNo;
    private String currency;
    private Double exchangeRate;
    private Integer bankAccountId;
    private String note;
    private String accountingStatus;
    private String paymentStatus;
    private Double totalDebitAmount;
    private Double totalCreditAmount;
    private Double settledAmount;
    private String bankName;
    private String bankPayeeName;
    private String bankAccountNo;
    private String bankAccountType;
    private String bankAddress;
    private String bankIfscCode;
    private String bankSwiftCode;
    private String partyType;
    private Integer partyId;
    private String paymentMode;
    private String paymentRefNo;
}