package com.tekfilo.inventory.invoice.memopurchasereturninvoice.repository;

import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceMainEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface MemoPurchaseReturnInvoiceMainRepository extends JpaRepository<MemoPurchaseReturnInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select sm from MemoPurchaseReturnInvoiceMainEntity sm where sm.isDeleted = 0")
    Page<MemoPurchaseReturnInvoiceMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM MemoPurchaseReturnInvoiceMainEntity")
    List<MemoPurchaseReturnInvoiceMainEntity> findMain();

    @Query("select sm from MemoPurchaseReturnInvoiceMainEntity sm where sm.isDeleted = 0 and sm.supplierId = :supplierId ")
    List<MemoPurchaseReturnInvoiceMainEntity> getPendingInvoiceList(Integer supplierId);

    @Modifying
    @Transactional
    @Query("update MemoPurchaseReturnInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);


    @Modifying
    @Transactional
    @Query("update MemoPurchaseReturnInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:id)")
    void deleteMainById(Integer id, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update MemoPurchaseReturnInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM MemoPurchaseReturnInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<MemoPurchaseReturnInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);
}
