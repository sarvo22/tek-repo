package com.tekfilo.inventory.item.invoice.purchase.repository;

import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ItemPurchaseInvoiceChargesRepository extends JpaRepository<ItemPurchaseInvoiceChargesEntity, Integer> {

    @Query("select c from ItemPurchaseInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<ItemPurchaseInvoiceChargesEntity> findAllCharges(Integer invId);


    @Modifying
    @Transactional
    @Query("update ItemPurchaseInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemPurchaseInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<ItemPurchaseInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
