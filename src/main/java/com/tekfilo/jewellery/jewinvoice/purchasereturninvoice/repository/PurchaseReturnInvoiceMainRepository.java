package com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.repository;

import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceMainEntity;
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
public interface PurchaseReturnInvoiceMainRepository extends JpaRepository<PurchaseReturnInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select sm from PurchaseReturnInvoiceMainEntity sm where sm.isDeleted = 0")
    Page<PurchaseReturnInvoiceMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM PurchaseReturnInvoiceMainEntity")
    List<PurchaseReturnInvoiceMainEntity> findMain();

    @Query("select sm from PurchaseReturnInvoiceMainEntity sm where sm.isDeleted = 0 and sm.supplierId = :supplierId ")
    List<PurchaseReturnInvoiceMainEntity> getPendingInvoiceList(Integer supplierId);

    @Modifying
    @Transactional
    @Query("update PurchaseReturnInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update PurchaseReturnInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM PurchaseReturnInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<PurchaseReturnInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);

}
