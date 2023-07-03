package com.tekfilo.inventory.invoice.memopurchaseinvoice.repository;

import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceMainEntity;
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
public interface MemoPurchaseInvoiceMainRepository extends JpaRepository<MemoPurchaseInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select sm from MemoPurchaseInvoiceMainEntity sm where sm.isDeleted = 0")
    Page<MemoPurchaseInvoiceMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM MemoPurchaseInvoiceMainEntity")
    List<MemoPurchaseInvoiceMainEntity> findMain();

    @Query("select sm from MemoPurchaseInvoiceMainEntity sm where sm.isDeleted = 0 and sm.supplierId = :supplierId ")
    List<MemoPurchaseInvoiceMainEntity> getPendingInvoiceList(Integer supplierId);

    @Query("FROM MemoPurchaseInvoiceMainEntity p where isDeleted = 0 and supplierId = :partyId and currency = :currency")
    List<MemoPurchaseInvoiceMainEntity> findAllInvoiceByPartyAndCurrency(Integer partyId, String currency);

    @Modifying
    @Transactional
    @Query("update MemoPurchaseInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update MemoPurchaseInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:id)")
    void deleteMainById(Integer id, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;


    @Query("select a from MemoPurchaseInvoiceMainEntity a where a.isDeleted = 0 and id in (:ids)")
    List<MemoPurchaseInvoiceMainEntity> findAllMainByMainIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("update MemoPurchaseInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIdList(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
