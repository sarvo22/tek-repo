package com.tekfilo.inventory.cut;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CutDto {
    private Integer id;
    private String cutName;
    private Integer groupCategory;
    private Integer operateBy;
}
