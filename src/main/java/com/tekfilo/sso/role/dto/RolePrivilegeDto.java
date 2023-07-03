package com.tekfilo.sso.role.dto;

import com.tekfilo.sso.role.entity.RoleEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class RolePrivilegeDto {
    private RoleEntity role;
    private Map<String, List<PrivilegeDto>> privilege;
    private Integer companyId;
    private Integer operateBy;
}
