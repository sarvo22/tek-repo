package com.tekfilo.jewellery.production.common;

import com.tekfilo.jewellery.master.BinEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class ProductionInvoiceDetailDto {

    private Integer id;
    private Integer invoiceId;
    private Integer jewId;
    private Integer sourceJewId;
    private Integer destJewId;
    private Integer sourceBinId;
    private Integer destBinId;
    private Integer binId;
    private BinEntity bin;
    private Double invQty;
    private BigDecimal labourAmount;
    private Double goldLossPct;
    private Double silverLossPct;
    private Double platinumLossPct;
    private Double adminPct;
    private BigDecimal grossWt;
    private BigDecimal totalCogsAmount;
}
