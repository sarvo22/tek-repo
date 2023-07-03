package com.tekfilo.inventory.shape;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ShapeDto {
    private Integer id;
    private String shapeName;
    private Integer groupCategory;
    private Integer operateBy;
}
