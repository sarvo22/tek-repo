package com.tekfilo.inventory.item.invoice.purchase.repository;

import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceMainEntity;
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
public interface ItemPurchaseInvoiceMainRepository extends JpaRepository<ItemPurchaseInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select sm from ItemPurchaseInvoiceMainEntity sm where sm.isDeleted = 0")
    Page<ItemPurchaseInvoiceMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM ItemPurchaseInvoiceMainEntity")
    List<ItemPurchaseInvoiceMainEntity> findMain();

    @Query("select sm from ItemPurchaseInvoiceMainEntity sm where sm.isDeleted = 0 and sm.supplierId = :supplierId ")
    List<ItemPurchaseInvoiceMainEntity> getPendingInvoiceList(Integer supplierId);

    @Modifying
    @Transactional
    @Query("update ItemPurchaseInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Query("FROM ItemPurchaseInvoiceMainEntity p where isDeleted = 0 and supplierId = :partyId and currency = :currency")
    List<ItemPurchaseInvoiceMainEntity> findAllInvoiceByPartyAndCurrency(Integer partyId, String currency);

    @Modifying
    @Transactional
    @Query("update ItemPurchaseInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:id)")
    void deleteMainById(Integer id, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update ItemPurchaseInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemPurchaseInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<ItemPurchaseInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);
}
