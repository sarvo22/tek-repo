package com.tekfilo.jewellery.order.purchase.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
public class PurchaseOrderDetailMouldPartDto {

    private Integer id;
    private Integer purchaseOrderDetailId;
    private Integer purchaseOrderId;
    private String partName;
    private String partNo;
    private String description;
    private Double pieces;
}
