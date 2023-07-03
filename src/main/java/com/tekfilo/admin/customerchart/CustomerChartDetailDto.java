package com.tekfilo.admin.customerchart;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CustomerChartDetailDto {

    private Integer id;
    private Integer customerChartId;
    private Integer commodityGroupId;
    private String markupPctType;
    private Double markupPctValue;
    private Double roundOff;
}
