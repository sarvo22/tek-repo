package com.tekfilo.admin.factorychart;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FactoryChartDetailDto {

    private Integer id;
    private Integer factoryChartId;
    private String labourType;
    private String amountPctType;
    private Double amountPctValue;
}
