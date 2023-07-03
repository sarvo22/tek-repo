package com.tekfilo.inventory.mixing;

import com.tekfilo.inventory.product.ProductDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MixingDetailDto {
    private Integer rowId;
    private Integer id;
    private Integer mixingId;

    private Integer sourceBinId;
    private Integer destinationBinId;

    private Integer sourceProductId;
    private Double sourceStockQty1;
    private Double sourceStockQty2;


    private Integer destinationProductId;
    private Double destinationStockQty1;
    private Double destinationStockQty2;

    private String sourceImageUrl;
    private Double sourceCostPrice;
    private String destinationImageUrl;
    private Double destinationCostPrice;

    private Double mixingQty1;
    private Double mixingQty2;

    private ProductDto sourceProduct;
    private ProductDto destinationProduct;

}
