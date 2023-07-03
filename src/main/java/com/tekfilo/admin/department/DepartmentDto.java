package com.tekfilo.admin.department;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class DepartmentDto implements Serializable {
    private Integer id;
    private String departmentName;
    private String remarks;
    private Integer sequence;
    private Integer isLocked;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer isDeleted;
    private Integer operateBy;

}
