package com.tekfilo.account.schedule;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Getter
@Setter
public class ScheduleDto {
    private Integer id;
    private String scheduleName;
    private String scheduleNature;
    private String debitCreditType;
    private String description;
    private Integer companyId;
    private Integer sequence;
    private Integer isLocked;
}
