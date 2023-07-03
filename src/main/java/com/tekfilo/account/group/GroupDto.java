package com.tekfilo.account.group;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GroupDto {
    private Integer id;
    private String groupName;
    private String groupNature;
    private String debitCreditType;
    private String description;
    private Integer scheduleId;
    private Integer companyId;
    private Integer sequence;
    private Integer isLocked;
}
