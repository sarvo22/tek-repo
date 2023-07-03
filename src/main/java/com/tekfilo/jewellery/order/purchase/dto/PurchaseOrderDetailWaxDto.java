package com.tekfilo.jewellery.order.purchase.dto;

import com.tekfilo.jewellery.master.KaratageEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
public class PurchaseOrderDetailWaxDto {

    private Integer id;
    private Integer purchaseOrderDetailId;
    private Integer purchaseOrderId;
    private String productSize;
    private Integer karatageId;
    private String description;
    private Double waxWt;
    private Double metalWt;
}