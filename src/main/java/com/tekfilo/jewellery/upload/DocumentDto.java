package com.tekfilo.jewellery.upload;

import lombok.Data;

@Data
public class DocumentDto {
    private Integer invoiceId;
    private String invoiceType;
    private String documentUrls;
    private String uploadModule;
}
