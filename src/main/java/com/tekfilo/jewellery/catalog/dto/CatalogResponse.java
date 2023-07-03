package com.tekfilo.jewellery.catalog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CatalogResponse implements Serializable {
    private String productName;
    private String productType;
    private Double diamondWt;
    private Double gemstoneWt;
    private Double metalWt;
    private String metalKaratage;
    private Double costPrice;
    private Double salesPrice;
    private Double markupPct;
    private Double markupPrice;
    private String imageUrl;
}
