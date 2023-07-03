package com.tekfilo.account.transaction.jv.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JVDetailDto {

    private Integer id;
    private Integer jvMainId;
    private Integer accountId;
    private String description;
    private BigDecimal taxPct;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal debitTaxAmount;
    private BigDecimal creditTaxAmount;
    private Integer sequence;
    private Integer isLocked;
}
