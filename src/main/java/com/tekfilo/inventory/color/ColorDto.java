package com.tekfilo.inventory.color;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ColorDto {
    private Integer id;
    private String colorName;
    private Integer groupCategory;
    private Integer operateBy;
}
