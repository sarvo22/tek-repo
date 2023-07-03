package com.tekfilo.sso.auth;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AuthForm implements Serializable {

    private String username;
    private String password;

}
