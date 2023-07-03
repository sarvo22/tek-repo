package com.tekfilo.admin.customer.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class CustomerDto implements Serializable {

    private Integer id;
    private String customerCode;
    private String customerName;
    private String customerAliasName;
    private Integer paymentTypeId;
    private Integer headSalesmanId;
    private Integer salesmanId;
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
