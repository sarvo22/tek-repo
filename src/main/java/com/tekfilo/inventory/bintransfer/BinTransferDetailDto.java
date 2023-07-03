package com.tekfilo.inventory.bintransfer;

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
    private Double sourceStockQty1;
    private Double sourceStockQty2;
    private String productNo;
    private Double purchasePrice;
    private String uom1;
    private String uom2;
    private Double transferQty1;
    private Double transferQty2;
    private String imageUrl;
    private Double costPrice;
    private Double sellingPrice;
    private Integer operateBy;
    private String description;
    private Double destinationStockQty1;
    private Double destinationStockQty2;
    private String picture1Path;


}
