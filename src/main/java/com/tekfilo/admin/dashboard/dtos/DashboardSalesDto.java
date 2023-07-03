package com.tekfilo.admin.dashboard.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DashboardSalesDto {

    private String dbgroup;
    private String group1;
    private Double amt;
    private Double qty1;
    private Double qty2;
    private Double cp;
    private Double gp;
    private Double gppct;
    private Double today;
}
