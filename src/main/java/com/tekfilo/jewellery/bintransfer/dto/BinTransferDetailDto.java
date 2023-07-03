package com.tekfilo.jewellery.bintransfer.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BinTransferDetailDto {
    private Integer id;
    private Integer rowId;
    private Integer transferId;
    private Integer sourceBinId;
    private Integer destinationBinId;
    private Integer productId;
    private Double sourceStockQty;
    private String productNo;
    private Double purchasePrice;
    private String uom;
    private Double transferQty;
    private String imageUrl;
    private Double costPrice;
    private Double sellingPrice;
    private Integer operateBy;
    private String description;
    private Double destinationStockQty;
    private String picturePath;
}
