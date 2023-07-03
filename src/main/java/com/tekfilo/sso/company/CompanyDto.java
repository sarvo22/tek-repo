package com.tekfilo.sso.company;

import lombok.Data;

import java.io.Serializable;

@Data
public class CompanyDto implements Serializable {


    private Integer id;
    private String companyName;
    private String displayName;
    private String businessType;
    private String companyType;
    private String defaultCurrency;
    private Integer countryId;
    private String state;
    private String city;
    private String street;
    private String area;
    private String pinCode;
    private String primeContactName;
    private String email;
    private String phoneNumber;
    private String mobileNo;
    private String website;
    private String registerType;
    private String registerNo;
    private String taxType;
    private String taxNo;
    private String status;
    private String dateFormat;
    private String uiDateFormat;
    private String timeZone;
    private String remarks;
    private String systemRemarks;
    private Integer isDeleted;
    private Integer operateBy;
    private String faxNo;
    private String fiscalYear;
    private String paymentAddress;
    private String dateTimeDivider;
    private String logoUrl;
}
