package com.tekfilo.admin.customer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDocumentDto {
    private Integer id;
    private Integer customerId;
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
