package com.tekfilo.admin.vouchertype;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VoucherTypeDto {
    private String voucherType;
    private String voucherName;
    private String voucherGroup;
    private Integer voucherStartNo;
}
