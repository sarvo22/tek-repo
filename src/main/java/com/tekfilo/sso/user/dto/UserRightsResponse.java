package com.tekfilo.sso.user.dto;

import com.tekfilo.sso.plan.entity.SubscriptionPrivilege;
import com.tekfilo.sso.role.entity.RoleEntity;
import com.tekfilo.sso.role.entity.RolePrivilegeEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserRightsResponse {

    private List<UserMenusDto> subscribedMenus;
    private List<SubscriptionPrivilege> subscribedPrivileges;
    private List<RoleEntity> roles;
    private List<RolePrivilegeEntity> rolePrivileges;
    private String xtenantId;
    private boolean status;
}
