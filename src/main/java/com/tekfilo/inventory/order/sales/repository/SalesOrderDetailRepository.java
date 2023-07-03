package com.tekfilo.inventory.order.sales.repository;

import com.tekfilo.inventory.order.sales.entity.SalesOrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetailEntity, Integer> {


    @Query("select d from SalesOrderDetailEntity d where d.isDeleted = 0 and d.salesOrderId = :salesOrderId")
    List<SalesOrderDetailEntity> findAllByMain(Integer salesOrderId);

    @Transactional
    @Modifying
    @Query("update SalesOrderDetailEntity set isDeleted = 1, modifiedBy = :operateBy, modifiedDt = current_timestamp where salesOrderId = :salesOrderId")
    void removeByMain(Integer salesOrderId, Integer operateBy);

    @Transactional
    @Modifying
    @Query("update SalesOrderDetailEntity set isDeleted = 1, modifiedBy = :operateBy, modifiedDt = current_timestamp where id = :purchaseOrderDetailId")
    void remove(Integer purchaseOrderDetailId, Integer operateBy);

    @Modifying
    @Transactional
    @Query("update SalesOrderDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and salesOrderId in (:ids)")
    void deleteDetailBySalesOrderId(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
