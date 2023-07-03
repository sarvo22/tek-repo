package com.tekfilo.inventory.invoice.salesreturninvoice.repository;

import com.tekfilo.inventory.invoice.salesreturninvoice.entity.SalesReturnInvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SalesReturnInvoiceDetailRepository extends JpaRepository<SalesReturnInvoiceDetailEntity, Integer> {
    @Query("select a from SalesReturnInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<SalesReturnInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("from SalesReturnInvoiceDetailEntity a where a.isDeleted = 0 and  referenceInvoiceId = :referenceInvoiceId and referenceInvoiceType = :referenceInvoiceType")
    List<SalesReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    @Modifying
    @Transactional
    @Query("update SalesReturnInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM SalesReturnInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<SalesReturnInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);
}
