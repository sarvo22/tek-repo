package com.tekfilo.inventory.upload;

import com.tekfilo.inventory.base.BaseDto;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class ProductDataUploadDto extends BaseDto implements Serializable {
    private Integer id;
    private String productNo;
    private String description;
    private String commodityId;
    private String commodityGroupId;
    private String sieveSize;
    private String mmSize;
    private String measurementLxbH;
    private String shapeId;
    private String cutId;
    private String colorId;
    private String clarityId;
    private String karatageId;
    private String caratWeight;
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
    private String exchangeRate;
    private String rapaportPrice;
    private Date rapaportDate;
    private String purity;
    private String unit1;
    private String unit2;
    private String comments;
    private String costPrice;
    private String salesPrice;
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
    private String minWt;
    private String maxWt;
    private String APrice;
    private String BPrice;
    private String prodCategory;
    private String defaultCurrency;
}
