package com.tekfilo.admin.currency;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CurrencyDto {
    private String code;
    private String name;
    private String symbol;
    private String formatter;
    private Integer decimalPlaces;
    private Integer operateBy;

}
