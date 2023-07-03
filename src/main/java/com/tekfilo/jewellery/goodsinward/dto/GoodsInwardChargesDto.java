package com.tekfilo.jewellery.goodsinward.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class GoodsInwardChargesDto {
    private Integer id;
    private Integer invId;
    private String chargeName;
    private Integer plusMinusFlag;
    private String inputPctAmountType;
    private BigDecimal inputPctAmountValue;
    private BigDecimal inputAmount;
    private Integer isPartyPayable;
    private Integer operateBy;
    private Integer isDeleted;
}
