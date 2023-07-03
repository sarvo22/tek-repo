package com.tekfilo.admin.customer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerLimitDto {
    private Integer id;
    private Integer customerId;
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
