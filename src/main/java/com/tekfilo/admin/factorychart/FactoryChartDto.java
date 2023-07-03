package com.tekfilo.admin.factorychart;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FactoryChartDto {

    private Integer id;
    private Integer factoryId;
    private String currency;
    private Double exchangeRate;
}
