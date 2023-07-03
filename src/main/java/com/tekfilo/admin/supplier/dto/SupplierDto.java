package com.tekfilo.admin.supplier.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class SupplierDto implements Serializable {

    private Integer id;
    private String supplierCode;
    private String supplierName;
    private String supplierAliasName;
    private Integer paymentTypeId;
    private Integer headBuyerId;
    private Integer buyerId;
    private String primaryContactName;
    private String primaryContactNo;
    private String businessType;
    private String businessNo;
    private String currency;
    private String zone;
    private String phoneNo;
    private String email;
    private String website;
    private String remarks;
    private String systemRemarks;
    private Integer sortSequence;
    private Integer isLocked;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer operateBy;
    private Integer isDeleted;
}
