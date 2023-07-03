package com.tekfilo.jewellery.jewinvoice.common;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceDetailDto {

    private Integer rowId;
    private Integer id;
    private Integer invId;
    private Integer productId;
    private String productDescription;
    private BigDecimal invQty;
    private String uom;
    private BigDecimal inputRate;
    private BigDecimal inputAmount;
    private String discountType;
    private BigDecimal discountValue;
    private Integer operateBy;
    private Integer isDeleted;
    private String productNo;
    private String description;
    private String picture1Path;
    private BigDecimal confQty;
    private BigDecimal retQty;
    private BigDecimal confInputRate;
    private BigDecimal confInputAmount;
    private BigDecimal retInputRate;
    private BigDecimal retInputAmount;
    private Integer binId;
    private String referenceInvoiceType;
    private Integer referenceInvoiceId;
    private Integer referenceInvoiceDetailId;
}
