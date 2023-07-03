package com.tekfilo.jewellery.production.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductionInvoiceComponentDto {
    private Integer id;
    private Integer invoiceId;
    private Integer invoiceDetailId;
    private Integer jewId;
    private Integer destJewId;
    private Integer lotId;
    private Integer binId;
    private Double consumedQty1;
    private Double consumedQty2;
    private Double brokenQty1;
    private Double brokenQty2;
    private Double lossQty1;
    private Double lossQty2;
    private Double originalBrokenQty1;
    private Double originalBrokenQty2;
    private Double originalLossQty1;
    private Double originalLossQty2;
    private Double returnQty1;
    private Double returnQty2;
    private Double totalQty1;
    private Double totalQty2;
    private Double cogsAmount;
    private Integer isCenterStone;
    private String uom1;
    private String uom2;
}
