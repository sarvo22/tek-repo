package com.tekfilo.inventory.invoice.common;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceDetailDto {

    private Integer rowId;
    private Integer id;
    private Integer invId;
    private Integer productId;
    private String productDescription;
    private BigDecimal invQty1;
    private BigDecimal invQty2;
    private String uom1;
    private String uom2;
    private BigDecimal inputRate;
    private BigDecimal inputAmount;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal ocpRate;
    private String commissionType;
    private BigDecimal commissionValue;
    private Integer operateBy;
    private Integer isDeleted;
    private String productNo;
    private String description;
    private String picture1Path;
    private BigDecimal confQty1;
    private BigDecimal confQty2;
    private BigDecimal retQty1;
    private BigDecimal retQty2;
    private BigDecimal confInputRate;
    private BigDecimal confInputAmount;
    private BigDecimal retInputRate;
    private BigDecimal retInputAmount;
    private Integer binId;
    private String referenceInvoiceType;
    private Integer referenceInvoiceId;
    private Integer referenceInvoiceDetailId;
}
