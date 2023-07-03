package com.tekfilo.jewellery.design.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class DesignComponentDto {

    private Integer id;
    private Integer designId;
    private Integer productId;
    private Integer commodityId;
    private Integer shapeId;
    private Integer colorId;
    private Integer cutId;
    private String settingType;
    private String sieveSize;
    private String mmSize;
    private Double qty1;
    private Double qty2;
    private Integer isCenterStone;
    private Integer settingTypeId;
    private Double totalWt;
}
