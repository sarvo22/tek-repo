package com.tekfilo.account.transaction.bankpaymentreceipt.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class BankPaymentReceiptDetailDto {
    private Integer id;
    private Integer invId;
    private Integer accountId;
    private String description;
    private BigDecimal taxPct;
    private BigDecimal grossAmount;
    private BigDecimal taxAmount;
    private  BigDecimal netAmount;
}