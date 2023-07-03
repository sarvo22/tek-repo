package com.tekfilo.jewellery.catalog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CatalogDto implements Serializable {
    private String catalogFor;
    private List<String> inputNos;
    private Integer productTypeId;
    private Integer commodityId;
    private Double diamondWtFrom;
    private Double diamondWtTo;
    private Integer metalKaratageId;
    private Double metalWtFrom;
    private Double metalWtTo;
}
