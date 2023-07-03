package com.tekfilo.inventory.clarity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ClarityDto {
    private Integer id;
    private String clarityName;
    private Integer groupCategory;
    private Integer operateBy;
}
