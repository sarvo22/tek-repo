package com.tekfilo.sso.user.dto;

import com.tekfilo.sso.company.CompanyEntity;
import com.tekfilo.sso.role.dto.PrivilegeDto;
import com.tekfilo.sso.usercompanymap.UserCompanyMapEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class UserDto implements Serializable {

    private String firstName;
    private String lastName;
    private String contactNo;
    private String username;
    private String password;
    private Integer id;
    private String email;
    private String[] roles;
    private String avatar;
    private List<UserCompanyMapEntity> userCompanies;
    private CompanyEntity loggedInCompany;
    private Map<String, PrivilegeDto> userRights;
}
