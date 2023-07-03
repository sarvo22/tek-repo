package com.tekfilo.inventory.item.invoice.purchase.repository;

import com.tekfilo.inventory.item.invoice.purchase.entity.ItemPurchaseInvoiceDetailEntity;
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
public interface ItemPurchaseInvoiceDetailRepository extends JpaRepository<ItemPurchaseInvoiceDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from ItemPurchaseInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<ItemPurchaseInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("select a from ItemPurchaseInvoiceDetailEntity a where a.isDeleted = 0 and a.productId = :productId")
    List<ItemPurchaseInvoiceDetailEntity> findAllDetailByProductId(Integer productId);

    @Modifying
    @Transactional
    @Query("update ItemPurchaseInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemPurchaseInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<ItemPurchaseInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);

    @Query("from ItemPurchaseInvoiceDetailEntity a where a.isDeleted = 0 and  referenceInvoiceId = :referenceInvoiceId and referenceInvoiceType = :referenceInvoiceType")
    List<ItemPurchaseInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
