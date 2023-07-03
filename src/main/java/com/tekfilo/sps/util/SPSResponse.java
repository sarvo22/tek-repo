package com.tekfilo.sps.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SPSResponse {
    private int status;
    private String message;
    private String responseOut;
}

