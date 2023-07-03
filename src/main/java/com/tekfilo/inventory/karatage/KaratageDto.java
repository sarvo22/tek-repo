package com.tekfilo.inventory.karatage;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class KaratageDto {
    private Integer id;
    private String karatageName;
    private Double impureToPureConvFactor;
    private Double pureToImpureConvFactor;
    private Integer operateBy;
}
