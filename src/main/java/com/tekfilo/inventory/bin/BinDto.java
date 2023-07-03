package com.tekfilo.inventory.bin;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BinDto {
    private Integer id;
    private String binName;
    private String binType;
    private Integer isDefault;
    private Integer operateBy;
}
