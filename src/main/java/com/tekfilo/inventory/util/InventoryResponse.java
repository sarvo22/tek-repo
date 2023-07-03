package com.tekfilo.inventory.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class InventoryResponse {

    private int status;
    private String message;
    private String langStatus;
    private Integer id;
    private String voucherTypeNo;
    private String voucherNo;
}
