package com.tekfilo.inventory.invoice.memosalesreturninvoice.repository;

import com.tekfilo.inventory.invoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MemoSalesReturnInvoiceChargesRepository extends JpaRepository<MemoSalesReturnInvoiceChargesEntity, Integer> {

    @Query("select c from MemoSalesReturnInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<MemoSalesReturnInvoiceChargesEntity> findAllCharges(Integer invId);

    @Modifying
    @Transactional
    @Query("update MemoSalesReturnInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM MemoSalesReturnInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<MemoSalesReturnInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
