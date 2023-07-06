package com.tekfilo.sso.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Email {
    private String toEmailId;
    private String tokenId;
    private Integer signupId;
    private String url;

    public Email(String toEmailId,
                 String tokenId,
                 Integer signupId,
                 String url) {
        this.toEmailId = toEmailId;
        this.tokenId = tokenId;
        this.signupId = signupId;
        this.url = url;

    }
}
