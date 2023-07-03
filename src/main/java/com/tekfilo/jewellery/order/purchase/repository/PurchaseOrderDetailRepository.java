package com.tekfilo.jewellery.order.purchase.repository;

import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetailEntity, Integer> {

    @Query("select d from PurchaseOrderDetailEntity d where d.isDeleted = 0 and d.purchaseOrderId = :purchaseOrderId")
    List<PurchaseOrderDetailEntity> findAllByMain(Integer purchaseOrderId);

    @Transactional
    @Modifying
    @Query("update PurchaseOrderDetailEntity set isDeleted = 1, modifiedBy = :operateBy, modifiedDt = current_timestamp where purchaseOrderId = :purchaseOrderId")
    void removeByMain(Integer purchaseOrderId, Integer operateBy);

    @Transactional
    @Modifying
    @Query("update PurchaseOrderDetailEntity set isDeleted = 1, modifiedBy = :operateBy, modifiedDt = current_timestamp where id = :purchaseOrderDetailId")
    void remove(Integer purchaseOrderDetailId, Integer operateBy);

    @Modifying
    @Transactional
    @Query("update PurchaseOrderDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDate=:modifiedDt where isDeleted = 0 and purchaseOrderId in (:ids)")
    void deleteDetailByMainIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt);
}
