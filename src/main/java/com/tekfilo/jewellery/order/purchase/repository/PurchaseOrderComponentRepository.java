package com.tekfilo.jewellery.order.purchase.repository;

import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PurchaseOrderComponentRepository extends JpaRepository<PurchaseOrderComponentEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM PurchaseOrderComponentEntity d where d.isDeleted = 0 and d.purchaseOrderId = :purchaseOrderId")
    List<PurchaseOrderComponentEntity> findAllByMainId(Integer purchaseOrderId);

    @Query("FROM PurchaseOrderComponentEntity d where d.isDeleted = 0 and d.purchaseOrderDetailId = :purchaseOrderDetailId")
    List<PurchaseOrderComponentEntity> findAllByDetailId(Integer purchaseOrderDetailId);

    @Modifying
    @Transactional
    @Query("update PurchaseOrderComponentEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDate=:modifiedDt where isDeleted = 0 and skuId in (:skuIds)")
    void deleteAllBySkuIds(List<Integer> skuIds, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update PurchaseOrderComponentEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDate=:modifiedDt where isDeleted = 0 and purchaseOrderId in (:ids)")
    void deleteAllByMainIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt);
}
