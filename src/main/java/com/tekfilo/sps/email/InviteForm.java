package com.tekfilo.sps.email;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class InviteForm {

    private String email;
    private String selectedRoleId;
    private String senderName;
    private String senderCompanyName;
    private String senderEmail;
    private Integer senderCompanyId;
    private String langage;
}
