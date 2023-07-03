package com.tekfilo.jewellery.order.purchase.repository;


import com.tekfilo.jewellery.order.purchase.entity.PurchaseOrderMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PurchaseOrderMainRepository extends JpaRepository<PurchaseOrderMainEntity, Integer>, JpaSpecificationExecutor {


    @Transactional
    @Modifying
    @Query("update PurchaseOrderMainEntity set isDeleted = 1, modifiedBy = :operateBy, modifiedDt = current_timestamp where id = :purchaseOrderId")
    void removeByMain(Integer purchaseOrderId, Integer operateBy);

    @Modifying
    @Transactional
    @Query("update PurchaseOrderMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDate=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteAllMainById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt);
}
