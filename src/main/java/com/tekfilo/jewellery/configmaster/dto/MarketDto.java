package com.tekfilo.jewellery.configmaster.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class MarketDto {
    private Integer id;
    private String marketName;
    private Integer companyId;
    private Integer sequence;
    private Integer isLocked;
    private Integer isDeleted;
}
