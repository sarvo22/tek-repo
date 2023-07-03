package com.tekfilo.inventory.invoice.purchasereturninvoice.repository;

import com.tekfilo.inventory.invoice.purchasereturninvoice.entity.PurchaseReturnInvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PurchaseReturnInvoiceDetailRepository extends JpaRepository<PurchaseReturnInvoiceDetailEntity, Integer> {
    @Query("select a from PurchaseReturnInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<PurchaseReturnInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("from PurchaseReturnInvoiceDetailEntity a where a.isDeleted = 0 and  referenceInvoiceId = :referenceInvoiceId and referenceInvoiceType = :referenceInvoiceType")
    List<PurchaseReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    @Modifying
    @Transactional
    @Query("update PurchaseReturnInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM PurchaseReturnInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<PurchaseReturnInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);
}
