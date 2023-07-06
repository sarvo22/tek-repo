package com.tekfilo.sso.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SSOResponse {
    private Integer id;
    private int status;
    private String message;
    private String email;
    private String url;
    private String langStatus;
}
