package com.tekfilo.jewellery.bintransfer.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class BinTransferDto {

    private Integer id;
    private String transferNo;
    private String transferDt;
    private Integer transferBy;
    private String remarks;
    private String currencyCode;
    private Double exchangeRate;
    private Integer destinationBinId;
    private Integer sourceBinId;
    private Integer sequence;
    private Integer isLocked;
    private Integer isDeleted;
    private Integer operateBy;
    private Map<String, Object> content;
    private String transferType;
    private BigDecimal totalQty;
}
