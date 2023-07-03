package com.tekfilo.account.transaction.jv.dto;

import lombok.Data;

import javax.persistence.Column;
import java.sql.Date;

@Data
public class JVMainDto {
    private Integer id;
    private String invoiceType;
    private String invoiceNo;
    private Date invoiceDate;
    private Date invoiceDueDate;
    private String referenceNo;
    private String currency;
    private Double exchangeRate;
    private Integer isLocked;
    private String note;
    private String accountingStatus;
    private String paymentStatus;
    private Double totalDebitAmount;
    private Double totalCreditAmount;
    private Double settledAmount;
    private String partyType;
    private Integer partyId;
    private String documentUrl;
}
