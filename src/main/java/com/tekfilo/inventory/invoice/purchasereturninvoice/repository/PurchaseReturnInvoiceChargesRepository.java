package com.tekfilo.inventory.invoice.purchasereturninvoice.repository;

import com.tekfilo.inventory.invoice.purchasereturninvoice.entity.PurchaseReturnInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PurchaseReturnInvoiceChargesRepository extends JpaRepository<PurchaseReturnInvoiceChargesEntity, Integer> {

    @Query("select c from PurchaseReturnInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<PurchaseReturnInvoiceChargesEntity> findAllCharges(Integer invId);

    @Modifying
    @Transactional
    @Query("update PurchaseReturnInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM PurchaseReturnInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<PurchaseReturnInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
