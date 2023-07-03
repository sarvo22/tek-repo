package com.tekfilo.jewellery.goodsinward.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class GoodsInwardRequestPayload {

    private GoodsInwardMainDto main;
    private List<GoodsInwardDetailDto> detail;
    private List<GoodsInwardChargesDto> charges;
}
