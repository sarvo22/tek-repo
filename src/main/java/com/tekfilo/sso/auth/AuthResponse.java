package com.tekfilo.sso.auth;

import com.tekfilo.sso.user.dto.UserDto;
import com.tekfilo.sso.user.entity.UserMenus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class AuthResponse {


    private int status;
    private String langStatus;
    private String accessToken;
    private String message;
    private String xTenantId;
    private UserDto data;
    private List<UserMenus> userMenus;
}
