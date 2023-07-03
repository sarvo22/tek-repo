package com.tekfilo.jewellery.order.sales.repository;


import com.tekfilo.jewellery.order.sales.entity.SalesOrderMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderMainRepository extends JpaRepository<SalesOrderMainEntity, Integer>, JpaSpecificationExecutor {


    @Query("update SalesOrderMainEntity set isDeleted = 1, modifiedBy = :operateBy, modifiedDt = current_timestamp where id = :salesOrderId")
    void removeByMain(Integer salesOrderId, Integer operateBy);
}
