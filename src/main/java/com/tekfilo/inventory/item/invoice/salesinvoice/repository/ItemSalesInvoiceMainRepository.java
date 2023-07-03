package com.tekfilo.inventory.item.invoice.salesinvoice.repository;

import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceMainEntity;
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
public interface ItemSalesInvoiceMainRepository extends JpaRepository<ItemSalesInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM ItemSalesInvoiceMainEntity")
    List<ItemSalesInvoiceMainEntity> findMain();

    @Query("select sm from ItemSalesInvoiceMainEntity sm where sm.isDeleted = 0 and sm.customerId = :customerId ")
    List<ItemSalesInvoiceMainEntity> getPendingInvoiceList(Integer customerId);

    @Modifying
    @Transactional
    @Query("update ItemSalesInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Query("FROM ItemSalesInvoiceMainEntity p where isDeleted = 0 and customerId = :partyId and currency = :currency")
    List<ItemSalesInvoiceMainEntity> findAllInvoiceByPartyAndCurrency(Integer partyId, String currency);

    @Modifying
    @Transactional
    @Query("update ItemSalesInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemSalesInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<ItemSalesInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);
}
