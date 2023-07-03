package com.tekfilo.inventory.product;

import com.tekfilo.inventory.base.BaseDto;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class ProductDto extends BaseDto implements Serializable {
    private Integer id;

    private String productNo;
    private String description;
    private Integer commodityId;
    private Integer commodityGroupId;
    private String sieveSize;
    private String mmSize;
    private String measurementLxbH;
    private Integer shapeId;
    private Integer cutId;
    private Integer colorId;
    private Integer clarityId;
    private Integer karatageId;
    private Double caratWeight;
    private String labName;
    private String certificateNo;
    private String treatment;
    private String transparency;
    private String origin;
    private String specificGravity;
    private String certificateImagePath;
    private String certificateUrl;
    private String polish;
    private String symmetry;
    private String fluorescence;
    private String inscriptionNo;
    private String tableDescription;
    private String currency;
    private Double exchangeRate;
    private Double rapaportPrice;
    private Date rapaportDate;
    private String purity;
    private String unit1;
    private String unit2;
    private String comments;
    private Double costPrice;
    private Double salesPrice;
    private String picture1Path;
    private String picture2Path;
    private String picture3Path;
    private String picture4Path;
    private String video1Path;
    private String video2Path;
    private String stoneType;

    private String measurementL;
    private String measurementB;
    private String measurementH;
    private String depth;
    private Double minWt;
    private Double maxWt;

    private Double APrice;
    private Double BPrice;

    private String prodCategory;
}
