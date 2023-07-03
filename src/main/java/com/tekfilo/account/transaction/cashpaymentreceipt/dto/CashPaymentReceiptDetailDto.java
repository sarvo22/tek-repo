package com.tekfilo.account.transaction.cashpaymentreceipt.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
@Getter
@Setter
public class CashPaymentReceiptDetailDto {
    private Integer id;
    private Integer invId;
    private Integer accountId;
    private String description;
    private BigDecimal taxPct;
    private BigDecimal grossAmount;
    private BigDecimal taxAmount;
    private  BigDecimal netAmount;
}
