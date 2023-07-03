package com.tekfilo.account.transaction.debitcreditnote.dto;

import lombok.Data;

@Data
public class DebitCreditNoteBreakupDto {
    private Integer id;
    private String invoiceType;
    private Integer invoiceDetailId;
    private Integer costCenterId;
    private Integer costCategoryId;
    private String description;
    private Double taxPct;
    private Double grossAmount;
    private Double taxAmount;
    private Double netAmount;
}
