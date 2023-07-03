package com.tekfilo.admin.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AdminResponse {

    private int status;
    private String message;
    private String langStatus;
    private Integer id;

}
