package com.tekfilo.sps.ibot.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Data
@Getter
@Setter
public class Rapaport {

    private String shape;
    private String color;
    private String clarity;
    private Double highSize;
    private Double lowSize;
    private Double price;
    private String currency;
    private Date date;
    private Integer companyId;
}
