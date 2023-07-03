package com.tekfilo.jewellery.bintransfer.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class BinTransferCommonDto {

    private BinTransferDto mainInfo;
    private List<BinTransferDetailDto> detailInfo;
    private Integer operateBy;
}
