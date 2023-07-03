package com.tekfilo.inventory.invoice.common;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceChargesDto {

    private Integer id;
    private Integer invId;
    private String chargeName;
    private Integer plusMinusFlag;
    private String inputPctAmountType;
    private BigDecimal inputPctAmountValue;
    private BigDecimal inputAmount;
    private Integer isSupplierPayable;
    private Integer isCustomerPayable;
    private Integer operateBy;
    private Integer isDeleted;
}
