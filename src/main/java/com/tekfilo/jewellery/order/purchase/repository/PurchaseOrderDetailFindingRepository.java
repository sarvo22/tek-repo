package com.tekfilo.jewellery.order.purchase.repository;

import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderDetailFindingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderDetailFindingRepository extends JpaRepository<PurchaseOrderDetailFindingEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM PurchaseOrderDetailFindingEntity d where d.isDeleted = 0 and d.purchaseOrderId = :purchaseOrderId")
    List<PurchaseOrderDetailFindingEntity> findAllByMainId(Integer purchaseOrderId);

    @Query("FROM PurchaseOrderDetailFindingEntity d where d.isDeleted = 0 and d.purchaseOrderDetailId = :purchaseOrderDetailId")
    List<PurchaseOrderDetailFindingEntity> findAllByDetailId(Integer purchaseOrderDetailId);
}
