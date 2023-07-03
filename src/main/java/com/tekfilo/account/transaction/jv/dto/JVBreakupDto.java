package com.tekfilo.account.transaction.jv.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JVBreakupDto {
    private Integer id;
    private String invoiceType;
    private Integer jvDetailId;
    private Integer costCenterId;
    private Integer costCategoryId;
    private String description;
    private Double taxPct;
    private Double debitAmount;
    private Double creditAmount;
    private Double debitTaxAmount;
    private Double creditTaxAmount;
}
