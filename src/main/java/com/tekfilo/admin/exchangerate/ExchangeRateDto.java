package com.tekfilo.admin.exchangerate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ExchangeRateDto {

    private Integer id;
    private String exchangeRateDate;
    private String currency;
    private Double exchangeRate;
    private Double bankBuyRate;
    private Double bankSellRate;
    private Double limitRate;
    private Integer isDeleted;
    private Integer operateBy;
}
