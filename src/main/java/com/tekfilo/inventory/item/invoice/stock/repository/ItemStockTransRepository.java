package com.tekfilo.inventory.item.invoice.stock.repository;

import com.tekfilo.inventory.item.invoice.stock.entity.ItemStockTransEntity;
import com.tekfilo.inventory.stock.StockTransEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemStockTransRepository extends JpaRepository<ItemStockTransEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM ItemStockTransEntity where isDeleted = 0 and companyId = :companyId and productId = :productId order by invoiceDate asc")
    List<ItemStockTransEntity> getProductTransListByProduct(Integer companyId, Integer productId);
}
