package com.tekfilo.admin.metalrate;

import lombok.Data;

import java.sql.Date;

@Data
public class MetalRateDto {
    private Integer id;
    private Date metalRateDt;
    private Integer commodityId;
    private String currency;
    private Double exchangeRate;
    private String uom;
    private Double inputRate;
    private Double metalRateIC;
    private Double metalRateLC;
    private String remarks;
    private Integer sequence;
    private Integer isLocked;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer isDeleted;
    private Integer operateBy;
}
