package com.tekfilo.admin.settings.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DefaultChargesDto {

    private Integer id;
    private String invType;
    private String chargeName;
    private Integer plusMinusFlag;
    private String inputPctAmountType;
    private BigDecimal inputPctAmountValue;
    private BigDecimal inputAmount;
    private Integer isPartyPayable;
    private Integer isSupplierPayable;
    private Integer isCustomerPayable;
}
