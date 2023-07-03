package com.tekfilo.sso.auth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class InviteForm {
    private String email;
    private String selectedRoleId;
    private String senderName;
    private String senderCompanyName;
    private String senderEmail;
    private Integer senderCompanyId;
    private String langage;

    private String username;
    private String password;
}
