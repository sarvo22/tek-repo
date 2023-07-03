package com.tekfilo.account.ifrsschedule;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class IfrsScheduleDto {
    private Integer id;
    private String scheduleName;
    private String scheduleNature;
    private String debitCreditType;
    private String description;
    private Integer companyId;
    private Integer sequence;
    private Integer isLocked;
}
