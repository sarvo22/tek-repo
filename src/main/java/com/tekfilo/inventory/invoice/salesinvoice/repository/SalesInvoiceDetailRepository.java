package com.tekfilo.inventory.invoice.salesinvoice.repository;

import com.tekfilo.inventory.invoice.salesinvoice.entity.SalesInvoiceDetailEntity;
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
public interface SalesInvoiceDetailRepository extends JpaRepository<SalesInvoiceDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from SalesInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<SalesInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("select a from SalesInvoiceDetailEntity a where a.isDeleted = 0 and a.productId = :productId")
    List<SalesInvoiceDetailEntity> findAllDetailByProductId(Integer productId);

    @Modifying
    @Transactional
    @Query("update SalesInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM SalesInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<SalesInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);

    @Query("from SalesInvoiceDetailEntity a where a.isDeleted = 0 and  referenceInvoiceId = :referenceInvoiceId and referenceInvoiceType = :referenceInvoiceType")
    List<SalesInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
