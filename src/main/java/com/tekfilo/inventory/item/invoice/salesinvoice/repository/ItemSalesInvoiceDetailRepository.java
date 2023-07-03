package com.tekfilo.inventory.item.invoice.salesinvoice.repository;

import com.tekfilo.inventory.item.invoice.salesinvoice.entity.ItemSalesInvoiceDetailEntity;
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
public interface ItemSalesInvoiceDetailRepository extends JpaRepository<ItemSalesInvoiceDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from ItemSalesInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<ItemSalesInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("select a from ItemSalesInvoiceDetailEntity a where a.isDeleted = 0 and a.productId = :productId")
    List<ItemSalesInvoiceDetailEntity> findAllDetailByProductId(Integer productId);

    @Modifying
    @Transactional
    @Query("update ItemSalesInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM ItemSalesInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<ItemSalesInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);

    @Query("from ItemSalesInvoiceDetailEntity a where a.isDeleted = 0 and  referenceInvoiceId = :referenceInvoiceId and referenceInvoiceType = :referenceInvoiceType")
    List<ItemSalesInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
