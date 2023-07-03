package com.tekfilo.jewellery.order.purchase.dto;

import com.tekfilo.jewellery.master.ProcessEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
public class PurchaseOrderDetailLabourDto {

    private Integer id;
    private Integer purchaseOrderDetailId;
    private Integer purchaseOrderId;
    private Integer processId;
    private String description;
    private Double qty;
    private Double rate;
    private Double amount;
}
