package com.tekfilo.admin.factory.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FactoryDocumentDto {
    private Integer id;
    private Integer factoryId;
    private String documentType;
    private String documentName;
    private String documentUrl;
    private String remarks;
    private String systemRemarks;
    private Integer sortSequence;
    private Integer isLocked;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer operateBy;
    private Integer isDeleted;
}
