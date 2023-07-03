package com.tekfilo.inventory.invoice.memosalesinvoice.repository;

import com.tekfilo.inventory.invoice.memosalesinvoice.entity.MemoSalesInvoiceDetailEntity;
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
public interface MemoSalesInvoiceDetailRepository extends JpaRepository<MemoSalesInvoiceDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from MemoSalesInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<MemoSalesInvoiceDetailEntity> findAllDetail(Integer id);

    @Modifying
    @Transactional
    @Query("update MemoSalesInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM MemoSalesInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<MemoSalesInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);
}
