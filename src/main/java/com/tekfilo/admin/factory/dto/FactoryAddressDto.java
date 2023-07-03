package com.tekfilo.admin.factory.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FactoryAddressDto {

    private int id;
    private Integer factoryId;
    private String addressType;
    private String address1;
    private String address2;
    private String country;
    private String state;
    private String city;
    private String area;
    private String pinCode;
    private Integer defaultAddress;
    private String remarks;
    private String systemRemarks;
    private Integer sortSequence;
    private Integer isLocked;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer operateBy;
    private Integer isDeleted;
}
