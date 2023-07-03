package com.tekfilo.inventory.item.invoice.salesreturn.repository;

import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceMainEntity;
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
public interface ItemSalesReturnInvoiceMainRepository extends JpaRepository<ItemSalesReturnInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM ItemSalesReturnInvoiceMainEntity")
    List<ItemSalesReturnInvoiceMainEntity> findMain();

    @Query("select sm from ItemSalesReturnInvoiceMainEntity sm where sm.isDeleted = 0 and sm.customerId = :customerId ")
    List<ItemSalesReturnInvoiceMainEntity> getPendingInvoiceList(Integer customerId);

    @Modifying
    @Transactional
    @Query("update ItemSalesReturnInvoiceMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update ItemSalesReturnInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemSalesReturnInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<ItemSalesReturnInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);
}
