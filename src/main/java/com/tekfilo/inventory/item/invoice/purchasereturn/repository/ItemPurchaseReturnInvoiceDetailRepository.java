package com.tekfilo.inventory.item.invoice.purchasereturn.repository;

import com.tekfilo.inventory.item.invoice.purchasereturn.entity.ItemPurchaseReturnInvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ItemPurchaseReturnInvoiceDetailRepository extends JpaRepository<ItemPurchaseReturnInvoiceDetailEntity, Integer> {
    @Query("select a from ItemPurchaseReturnInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<ItemPurchaseReturnInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("from ItemPurchaseReturnInvoiceDetailEntity a where a.isDeleted = 0 and  referenceInvoiceId = :referenceInvoiceId and referenceInvoiceType = :referenceInvoiceType")
    List<ItemPurchaseReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    @Modifying
    @Transactional
    @Query("update ItemPurchaseReturnInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemPurchaseReturnInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<ItemPurchaseReturnInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);
}
