package com.tekfilo.jewellery.jewinvoice.memosalesinvoice.repository;

import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceMainEntity;
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
public interface MemoSalesInvoiceMainRepository extends JpaRepository<MemoSalesInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select sm from MemoSalesInvoiceMainEntity sm where sm.isDeleted = 0")
    Page<MemoSalesInvoiceMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM MemoSalesInvoiceMainEntity")
    List<MemoSalesInvoiceMainEntity> findMain();

    @Query("select sm from MemoSalesInvoiceMainEntity sm where sm.isDeleted = 0 and sm.customerId = :customerId ")
    List<MemoSalesInvoiceMainEntity> getPendingInvoiceList(Integer customerId);

    @Query("FROM MemoSalesInvoiceMainEntity p where isDeleted = 0 and customerId = :partyId and currency = :currency")
    List<MemoSalesInvoiceMainEntity> findAllInvoiceByPartyAndCurrency(Integer partyId, String currency);

    @Modifying
    @Transactional
    @Query("update MemoSalesInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update MemoSalesInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM MemoSalesInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<MemoSalesInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update MemoSalesInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:id)")
    void deleteMainById(Integer id, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

}
