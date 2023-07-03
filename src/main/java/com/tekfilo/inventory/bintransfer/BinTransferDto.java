package com.tekfilo.inventory.bintransfer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
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
}
