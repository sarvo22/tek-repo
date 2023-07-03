package com.tekfilo.jewellery.goodsoutward.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class GoodsOutwardRequestPayload {

    private GoodsOutwardMainDto main;
    private List<GoodsOutwardDetailDto> detail;
    private List<GoodsOutwardChargesDto> charges;
}
