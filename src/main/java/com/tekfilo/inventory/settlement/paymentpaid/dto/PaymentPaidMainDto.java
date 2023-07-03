package com.tekfilo.inventory.settlement.paymentpaid.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
public class PaymentPaidMainDto {

    private Integer id;
    private String paymentType;
    private String paymentNo;
    private String referenceNo;
    private Date paymentDate;
    private Integer supplierId;
    private String paymentMode;
    private String paidThrough;
    private String currency;
    private Double exchangeRate;
    private String note;
    private BigDecimal amountPaid;
    private BigDecimal bankCharges;
    private Integer bankCashAccountId;
    private String documentUrl;
}
