package com.tekfilo.account.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AccountResponse {
    private int status;
    private String message;
    private String langStatus;
    private Integer id;
}
