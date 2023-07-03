package com.tekfilo.jewellery.order.purchase.repository;

import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderDetailMouldPartEntity;
import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderDetailWaxEntity;
import com.tekfilo.jewellery.sku.entity.SkuWaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderDetailWaxRepository extends JpaRepository<PurchaseOrderDetailWaxEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM PurchaseOrderDetailWaxEntity d where d.isDeleted = 0 and d.purchaseOrderId = :purchaseOrderId")
    List<PurchaseOrderDetailWaxEntity> findAllByMainId(Integer purchaseOrderId);

    @Query("FROM PurchaseOrderDetailWaxEntity d where d.isDeleted = 0 and d.purchaseOrderDetailId = :purchaseOrderDetailId")
    List<PurchaseOrderDetailWaxEntity> findAllByDetailId(Integer purchaseOrderDetailId);

}
