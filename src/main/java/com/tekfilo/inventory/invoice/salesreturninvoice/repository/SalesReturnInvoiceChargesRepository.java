package com.tekfilo.inventory.invoice.salesreturninvoice.repository;

import com.tekfilo.inventory.invoice.salesreturninvoice.entity.SalesReturnInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SalesReturnInvoiceChargesRepository extends JpaRepository<SalesReturnInvoiceChargesEntity, Integer> {

    @Query("select c from SalesReturnInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<SalesReturnInvoiceChargesEntity> findAllCharges(Integer invId);

    @Modifying
    @Transactional
    @Query("update SalesReturnInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM SalesReturnInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<SalesReturnInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
