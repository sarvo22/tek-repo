package com.tekfilo.jewellery.jewinvoice.salesinvoice.repository;

import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceMainEntity;
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
public interface SalesInvoiceMainRepository extends JpaRepository<SalesInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM SalesInvoiceMainEntity")
    List<SalesInvoiceMainEntity> findMain();

    @Query("select sm from SalesInvoiceMainEntity sm where sm.isDeleted = 0 and sm.customerId = :customerId ")
    List<SalesInvoiceMainEntity> getPendingInvoiceList(Integer customerId);

    @Modifying
    @Transactional
    @Query("update SalesInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Query("FROM SalesInvoiceMainEntity p where isDeleted = 0 and customerId = :partyId and currency = :currency")
    List<SalesInvoiceMainEntity> findAllInvoiceByPartyAndCurrency(Integer partyId, String currency);

    @Modifying
    @Transactional
    @Query("update SalesInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM SalesInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<SalesInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);
}
