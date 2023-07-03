package com.tekfilo.inventory.order.sales.repository;


import com.tekfilo.inventory.order.sales.entity.SalesOrderMainEntity;
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
public interface SalesOrderMainRepository extends JpaRepository<SalesOrderMainEntity, Integer>, JpaSpecificationExecutor {


    @Transactional
    @Modifying
    @Query("update SalesOrderMainEntity set isDeleted = 1, modifiedBy = :operateBy, modifiedDt = current_timestamp where id = :salesOrderId")
    void removeByMain(Integer salesOrderId, Integer operateBy);


    @Modifying
    @Transactional
    @Query("update SalesOrderMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
