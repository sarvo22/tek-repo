package com.tekfilo.jewellery.order.purchase.repository;

import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderDetailLabourEntity;
import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderDetailMouldPartEntity;
import com.tekfilo.jewellery.sku.entity.SkuMouldPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderDetailMouldPartRepository extends JpaRepository<PurchaseOrderDetailMouldPartEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM PurchaseOrderDetailMouldPartEntity d where d.isDeleted = 0 and d.purchaseOrderId = :purchaseOrderId")
    List<PurchaseOrderDetailMouldPartEntity> findAllByMainId(Integer purchaseOrderId);

    @Query("FROM PurchaseOrderDetailMouldPartEntity d where d.isDeleted = 0 and d.purchaseOrderDetailId = :purchaseOrderDetailId")
    List<PurchaseOrderDetailMouldPartEntity> findAllByDetailId(Integer purchaseOrderDetailId);
}
