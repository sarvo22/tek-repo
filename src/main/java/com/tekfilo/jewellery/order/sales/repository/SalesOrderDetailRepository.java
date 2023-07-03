package com.tekfilo.jewellery.order.sales.repository;

import com.tekfilo.jewellery.order.sales.entity.SalesOrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetailEntity, Integer> {

    @Query("select d from SalesOrderDetailEntity d where d.isDeleted = 0 and d.salesOrderId = :salesOrderId")
    List<SalesOrderDetailEntity> findAllByMain(Integer salesOrderId);

    @Query("update SalesOrderDetailEntity set isDeleted = 1, modifiedBy = :operateBy, modifiedDt = current_timestamp where salesOrderId = :salesOrderId")
    void removeByMain(Integer salesOrderId, Integer operateBy);

    @Query("update SalesOrderDetailEntity set isDeleted = 1, modifiedBy = :operateBy, modifiedDt = current_timestamp where id = :salesOrderDetailId")
    void remove(Integer salesOrderDetailId, Integer operateBy);
}
