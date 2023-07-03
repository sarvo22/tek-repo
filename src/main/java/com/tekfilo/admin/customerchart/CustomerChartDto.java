package com.tekfilo.admin.customerchart;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CustomerChartDto {

    private Integer id;
    private Integer customerId;
    private String currency;
    private Double exchangeRate;
}
