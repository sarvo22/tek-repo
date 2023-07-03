package com.tekfilo.jewellery.order.purchase.repository;

import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderDetailFindingEntity;
import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderDetailLabourEntity;
import com.tekfilo.jewellery.sku.entity.SkuLabourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderDetailLabourRepository extends JpaRepository<PurchaseOrderDetailLabourEntity, Integer>, JpaSpecificationExecutor {

        @Query("FROM PurchaseOrderDetailLabourEntity d where d.isDeleted = 0 and d.purchaseOrderId = :purchaseOrderId")
        List<PurchaseOrderDetailLabourEntity> findAllByMainId(Integer purchaseOrderId);

        @Query("FROM PurchaseOrderDetailLabourEntity d where d.isDeleted = 0 and d.purchaseOrderDetailId = :purchaseOrderDetailId")
        List<PurchaseOrderDetailLabourEntity> findAllByDetailId(Integer purchaseOrderDetailId);

}
