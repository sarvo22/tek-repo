package com.tekfilo.jewellery.order.purchase.dto;

import com.tekfilo.jewellery.configmaster.entity.SettingTypeEntity;
import com.tekfilo.jewellery.master.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
public class PurchaseOrderComponentDto {

    private Integer id;
    private Integer purchaseOrderDetailId;
    private Integer purchaseOrderId;
    private Integer productId;
    private Integer commodityId;
    private Integer shapeId;
    private Integer colorId;
    private Integer cutId;
    private Integer settingTypeId;
    private String sieveSize;
    private String mmSize;
    private Double qty1;
    private Double qty2;
    private Double totalWt;
    private Integer isCenterStone;
}
