package com.tekfilo.inventory.item.invoice.purchasereturn.repository;

import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceMainEntity;
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
public interface ItemPurchaseReturnInvoiceMainRepository extends JpaRepository<ItemPurchaseReturnInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select sm from ItemPurchaseReturnInvoiceMainEntity sm where sm.isDeleted = 0")
    Page<ItemPurchaseReturnInvoiceMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM ItemPurchaseReturnInvoiceMainEntity")
    List<ItemPurchaseReturnInvoiceMainEntity> findMain();

    @Query("select sm from ItemPurchaseReturnInvoiceMainEntity sm where sm.isDeleted = 0 and sm.supplierId = :supplierId ")
    List<ItemPurchaseReturnInvoiceMainEntity> getPendingInvoiceList(Integer supplierId);

    @Modifying
    @Transactional
    @Query("update ItemPurchaseReturnInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update ItemPurchaseReturnInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemPurchaseReturnInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<ItemPurchaseReturnInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);
}
