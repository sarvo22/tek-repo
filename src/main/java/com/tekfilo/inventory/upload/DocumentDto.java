package com.tekfilo.inventory.upload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DocumentDto {
    private Integer invoiceId;
    private String invoiceType;
    private String documentUrls;
    private String uploadModule;
}
