package com.tekfilo.inventory.item.invoice.common;

import lombok.Data;

import java.util.List;

@Data
public class ItemInvoiceRequestPayload {

    private ItemInvoiceMainDto main;
    private List<ItemInvoiceDetailDto> detail;
    private List<ItemInvoiceChargesDto> charges;
}
