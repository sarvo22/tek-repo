package com.tekfilo.inventory.mixing;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
@Getter
@Setter
public class MixingDto {

    private Integer id;
    private String mixingType;
    private String mixingNo;
    private String mixingDt;
    private Integer mixingBy;
    private String remarks;
    private String currencyCode;
    private Double exchangeRate;
    private Integer sequence;
    private Integer isLocked;
    private Integer isDeleted;
    private Integer operateBy;
    private Map<String, Object> content;
}
