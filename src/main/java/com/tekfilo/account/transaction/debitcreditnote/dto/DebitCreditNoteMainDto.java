package com.tekfilo.account.transaction.debitcreditnote.dto;

import lombok.Data;

import javax.persistence.Column;
import java.sql.Date;

@Data
public class DebitCreditNoteMainDto {

    private Integer id;
    private String invoiceType;
    private String invoiceNo;
    private Date invoiceDate;
    private Date invoiceDueDate;
    private Integer invDueMonths;
    private Integer invDueDays;
    private String referenceNo;
    private String currency;
    private Double exchangeRate;
    private String partyType;
    private Integer partyId;
    private Integer partyAccountId;
    private Integer employeeId;
    private Integer paymentTypeId;
    private String paymentStatus;
    private String accountingStatus;
    private Integer billingId;
    private String billingAddress1;
    private String billingAddress2;
    private String billingCountry;
    private String billingState;
    private String billingCity;
    private String billingStreet;
    private String billingPincode;
    private String billingWebsite;
    private String billingPhoneNo;
    private String billingFaxNo;
    private String billingMobileNo;
    private Integer shipingId;
    private String shipingAddress1;
    private String shipingAddress2;
    private String shipingCountry;
    private String shipingState;
    private String shipingCity;
    private String shipingStreet;
    private String shipingPincode;
    private String shipingWebsite;
    private String shipingPhoneNo;
    private String shipingFaxNo;
    private String shipingMobileNo;
    private String note;
    private Double totalDebitAmount;
    private Double totalCreditAmount;
    private Double settledAmount;
}
