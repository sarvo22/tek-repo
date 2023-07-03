package com.tekfilo.inventory.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransRepository extends JpaRepository<StockTransEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM StockTransEntity where isDeleted = 0 and companyId = :companyId and productId = :productId order by invoiceDate asc")
    List<StockTransEntity> getProductTransListByProduct(Integer companyId, Integer productId);
}
