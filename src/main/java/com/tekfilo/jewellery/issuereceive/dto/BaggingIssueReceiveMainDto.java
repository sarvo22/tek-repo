package com.tekfilo.jewellery.issuereceive.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BaggingIssueReceiveMainDto {
    private Integer id;
    private String invoiceType;
    private String invoiceNo;
    private String referenceNo;
    private Date invoiceDate;
    private String currency;
    private Double exchangeRate;
    private Integer factoryId;
    private Integer isExchange;
    private String note;
    private Integer sequence;
    private Integer isLocked;
    private Integer isDeleted;
}
