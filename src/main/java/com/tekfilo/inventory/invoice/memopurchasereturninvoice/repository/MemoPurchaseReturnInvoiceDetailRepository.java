package com.tekfilo.inventory.invoice.memopurchasereturninvoice.repository;

import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MemoPurchaseReturnInvoiceDetailRepository extends JpaRepository<MemoPurchaseReturnInvoiceDetailEntity, Integer> {
    @Query("select a from MemoPurchaseReturnInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<MemoPurchaseReturnInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("from MemoPurchaseReturnInvoiceDetailEntity a where a.isDeleted = 0 and  referenceInvoiceId = :referenceInvoiceId and referenceInvoiceType = :referenceInvoiceType")
    List<MemoPurchaseReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    @Modifying
    @Transactional
    @Query("update MemoPurchaseReturnInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM MemoPurchaseReturnInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<MemoPurchaseReturnInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);
}
