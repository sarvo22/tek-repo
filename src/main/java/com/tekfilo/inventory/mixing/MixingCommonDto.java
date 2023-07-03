package com.tekfilo.inventory.mixing;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MixingCommonDto {

    private MixingDto mainInfo;
    private List<MixingDetailDto> detailInfo;
    private Integer operateBy;
}
