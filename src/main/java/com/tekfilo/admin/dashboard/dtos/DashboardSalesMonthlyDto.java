package com.tekfilo.admin.dashboard.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DashboardSalesMonthlyDto {
    private String grp;
    private Double amt;
    private Double qty1;
    private Double qty2;

    private String[] series1;
    private String series2;
    private double[] amountSeries;
    private double[] qty1Series;
    private double[] qty2Series;
}
