package com.tekfilo.jewellery.jewinvoice.purchaseinvoice.repository;

import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
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
public interface PurchaseInvoiceMainRepository extends JpaRepository<PurchaseInvoiceMainEntity, Integer>, JpaSpecificationExecutor {
    @Query("select sm from PurchaseInvoiceMainEntity sm where sm.isDeleted = 0")
    Page<PurchaseInvoiceMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM PurchaseInvoiceMainEntity")
    List<PurchaseInvoiceMainEntity> findMain();

    @Query("select sm from PurchaseInvoiceMainEntity sm where sm.isDeleted = 0 and sm.supplierId = :supplierId ")
    List<PurchaseInvoiceMainEntity> getPendingInvoiceList(Integer supplierId);

    @Modifying
    @Transactional
    @Query("update PurchaseInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Query("FROM PurchaseInvoiceMainEntity p where isDeleted = 0 and supplierId = :partyId and currency = :currency")
    List<PurchaseInvoiceMainEntity> findAllInvoiceByPartyAndCurrency(Integer partyId, String currency);

    @Modifying
    @Transactional
    @Query("update PurchaseInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:id)")
    void deleteMainById(Integer id, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update PurchaseInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM PurchaseInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<PurchaseInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);

}
