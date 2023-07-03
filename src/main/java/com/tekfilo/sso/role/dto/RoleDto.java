package com.tekfilo.sso.role.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class RoleDto {
    private Integer roleId;
    private String roleName;
    private Integer subscriptionId;
    private String remarks;
    private String systemRemarks;
    private Integer createdBy;
    private Timestamp createdDate;
    private Integer modifiedBy;
    private Timestamp modifiedDate;
    private Integer isDeleted;
}
