package com.tekfilo.inventory.item.invoice.common;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemInvoiceChargesDto {

    private Integer id;
    private Integer invId;
    private String chargeName;
    private Integer plusMinusFlag;
    private String inputPctAmountType;
    private BigDecimal inputPctAmountValue;
    private BigDecimal inputAmount;
    private Integer isSupplierPayable;
    private Integer isCustomerPayable;
}
