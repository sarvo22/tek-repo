package com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice.repository;

import com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceMainEntity;
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
public interface MemoSalesReturnInvoiceMainRepository extends JpaRepository<MemoSalesReturnInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select sm from MemoSalesReturnInvoiceMainEntity sm where sm.isDeleted = 0")
    Page<MemoSalesReturnInvoiceMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM MemoSalesReturnInvoiceMainEntity")
    List<MemoSalesReturnInvoiceMainEntity> findMain();

    @Query("select sm from MemoSalesReturnInvoiceMainEntity sm where sm.isDeleted = 0 and sm.customerId = :customerId ")
    List<MemoSalesReturnInvoiceMainEntity> getPendingInvoiceList(Integer customerId);

    @Modifying
    @Transactional
    @Query("update MemoSalesReturnInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update MemoSalesReturnInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM MemoSalesReturnInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<MemoSalesReturnInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);

}
