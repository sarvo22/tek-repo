package com.tekfilo.jewellery.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
public class BaseDto {
    private String remarks;
    private Integer sequence;
    private Integer isLocked;
    private Integer createdBy;
    private Timestamp createdDt;
    private Integer modifiedBy;
    private Timestamp modifiedDt;
    private Integer isDeleted;
    private Integer operateBy;

}
