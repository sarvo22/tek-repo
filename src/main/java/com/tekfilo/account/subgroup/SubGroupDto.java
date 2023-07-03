package com.tekfilo.account.subgroup;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SubGroupDto {
    private Integer id;
    private String subGroupName;
    private String subGroupNature;
    private String debitCreditType;
    private String description;
    private Integer groupId;
    private Integer companyId;
    private Integer sequence;
    private Integer isLocked;
}
