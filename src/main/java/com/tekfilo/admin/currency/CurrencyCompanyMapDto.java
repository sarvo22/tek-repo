package com.tekfilo.admin.currency;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CurrencyCompanyMapDto {
    private Integer companyId;
    private String currencyCode;
    private Integer isDefault;
}
