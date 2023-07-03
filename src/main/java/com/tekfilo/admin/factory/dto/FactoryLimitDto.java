package com.tekfilo.admin.factory.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class FactoryLimitDto {
    private Integer id;
    private Integer factoryId;
    private String limitType;
    private String currency;
    private Double exchangeRate;
    private BigDecimal limitAmountIC;
    private BigDecimal limitAmountLC;
    private String remarks;
    private String systemRemarks;
    private Integer sortSequence;
    private Integer isLocked;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer operateBy;
    private Integer isDeleted;
}
