package com.tekfilo.jewellery.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class JewResponse {

    private int status;
    private String message;
    private String voucherTypeNo;
    private String langStatus;
    private Integer id;
}
