package com.tekfilo.jewellery.configmaster.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Data
@Getter
@Setter
public class ProductTypeDto {

    private Integer id;
    private String productTypeName;
    private String units;
    private Integer companyId;
    private Integer sequence;
    private Integer isLocked;
    private Integer isDeleted;
}
