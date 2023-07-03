package com.tekfilo.account.costcenter;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CostCenterDto {
    private Integer id;
    private String costCenterName;
    private String costCenterGroup;
    private Integer costCategoryId;
    private String description;
    private Integer companyId;
    private Integer sequence;
    private Integer isLocked;


}
