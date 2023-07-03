package com.tekfilo.sso.auth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SwitchCompanyResponse {
    private String xtenantId;
    private int status;
    private String message;
}
