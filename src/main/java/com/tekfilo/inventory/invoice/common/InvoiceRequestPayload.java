package com.tekfilo.inventory.invoice.common;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceRequestPayload {

    private InvoiceMainDto main;
    private List<InvoiceDetailDto> detail;
    private List<InvoiceChargesDto> charges;
}
