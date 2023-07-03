package com.tekfilo.account.transaction.common;

import lombok.Data;

import javax.persistence.Column;
import java.sql.Date;
@Data
public class PartyRegisterDto {

    private Integer invoiceId;
    private String invoiceType;
    private String invoiceNo;
    private Date invoiceDate;
    private Date invoiceDueDate;
    private Double invoiceDueMonths;
    private Double invoiceDueDays;
    private Integer paymentTypeId;
    private String partyType;
    private Integer partyId;
    private String status;
    private String referenceNo;
    private String currency;
    private Double exchangeRate;
    private Integer inOutFlag;
    private Double invoiceAmount;
    private Double settlementAmount;
    private String exchDiffCurrency;
    private Double exchDiffAmount;
}
