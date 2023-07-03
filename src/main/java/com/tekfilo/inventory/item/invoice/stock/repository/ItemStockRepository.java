package com.tekfilo.inventory.item.invoice.stock.repository;

import com.tekfilo.inventory.item.invoice.stock.entity.ItemStockEntity;
import com.tekfilo.inventory.stock.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemStockRepository extends JpaRepository<ItemStockEntity, Integer>, JpaSpecificationExecutor {
}
