package com.tekfilo.inventory.invoice.salesreturninvoice.repository;

import com.tekfilo.inventory.invoice.salesreturninvoice.entity.SalesReturnInvoiceMainEntity;
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
public interface SalesReturnInvoiceMainRepository extends JpaRepository<SalesReturnInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM SalesReturnInvoiceMainEntity")
    List<SalesReturnInvoiceMainEntity> findMain();

    @Query("select sm from SalesReturnInvoiceMainEntity sm where sm.isDeleted = 0 and sm.customerId = :customerId ")
    List<SalesReturnInvoiceMainEntity> getPendingInvoiceList(Integer customerId);

    @Modifying
    @Transactional
    @Query("update SalesReturnInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update SalesReturnInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM SalesReturnInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<SalesReturnInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);
}
