package com.tekfilo.sps.ibot.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
@Getter
@Setter
public class MetalRateResponse {

    private boolean success;
    private String timestamp;
    private String date;
    private String base;
    private Map<String, Double> rates;

    private String unit;

}
