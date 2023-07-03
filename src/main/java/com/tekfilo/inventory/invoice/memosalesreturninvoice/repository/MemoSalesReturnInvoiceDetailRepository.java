package com.tekfilo.inventory.invoice.memosalesreturninvoice.repository;

import com.tekfilo.inventory.invoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MemoSalesReturnInvoiceDetailRepository extends JpaRepository<MemoSalesReturnInvoiceDetailEntity, Integer> {
    @Query("select a from MemoSalesReturnInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<MemoSalesReturnInvoiceDetailEntity> findAllDetail(Integer id);

    @Modifying
    @Transactional
    @Query("update MemoSalesReturnInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM MemoSalesReturnInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<MemoSalesReturnInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);

    @Query("from MemoSalesReturnInvoiceDetailEntity a where a.isDeleted = 0 and  referenceInvoiceId = :referenceInvoiceId and referenceInvoiceType = :referenceInvoiceType")
    List<MemoSalesReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
