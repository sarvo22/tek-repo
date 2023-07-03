package com.tekfilo.jewellery.issuereceive.dto;

import lombok.Data;

@Data
public class BaggingIssueReceiveDetailDto {
    private Integer id;
    private Integer invId;
    private Integer jewId;
    private Integer lotId;
    private Integer binId;
    private Integer refInvoiceId;
    private Integer refInvoiceDetailId;
    private String refInvoiceType;
    private String description;
    private Double invQty;
    private String uom;
    private Double invQty1;
    private String uom1;
    private Double invQty2;
    private String uom2;
    private Integer sequence;
    private Integer isLocked;
    private Integer isDeleted;
}
