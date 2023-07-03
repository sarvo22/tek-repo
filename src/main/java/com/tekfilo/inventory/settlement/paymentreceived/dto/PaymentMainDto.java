package com.tekfilo.inventory.settlement.paymentreceived.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PaymentMainDto {

    private Integer id;
    private String paymentNo;
    private String paymentType;
    private String referenceNo;
    private Date paymentDate;
    private Integer customerId;
    private String paymentMode;
    private String depositTo;
    private String currency;
    private Double exchangeRate;
    private String note;
    private BigDecimal amountReceived;
    private BigDecimal bankCharges;
    private Integer bankCashAccountId;
}
