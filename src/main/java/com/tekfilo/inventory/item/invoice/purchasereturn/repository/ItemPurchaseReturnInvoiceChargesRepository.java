package com.tekfilo.inventory.item.invoice.purchasereturn.repository;

import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ItemPurchaseReturnInvoiceChargesRepository extends JpaRepository<ItemPurchaseReturnInvoiceChargesEntity, Integer> {

    @Query("select c from ItemPurchaseReturnInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<ItemPurchaseReturnInvoiceChargesEntity> findAllCharges(Integer invId);

    @Modifying
    @Transactional
    @Query("update ItemPurchaseReturnInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemPurchaseReturnInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<ItemPurchaseReturnInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
