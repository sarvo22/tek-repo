package com.tekfilo.admin.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {
    private Integer roleId;
    private String roleName;
    private String remarks;
    private String systemRemarks;
    private Integer createdBy;
    private Timestamp createdDate;
    private Integer modifiedBy;
    private Timestamp modifiedDate;
    private Integer isDeleted;
}
