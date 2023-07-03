package com.tekfilo.admin.supplier.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SupplierContactDto {
    private int id;
    private Integer supplierId;
    private String contactType;
    private String name;
    private String designation;
    private String phoneNo;
    private String mobileNo;
    private String email;
    private String remarks;
    private String systemRemarks;
    private Integer sortSequence;
    private Integer isLocked;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer operateBy;
    private Integer isDeleted;

}
