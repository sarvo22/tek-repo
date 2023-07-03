package com.tekfilo.jewellery.jewinvoice.salesinvoice.repository;

import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SalesInvoiceChargesRepository extends JpaRepository<SalesInvoiceChargesEntity, Integer> {

    @Query("select c from SalesInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<SalesInvoiceChargesEntity> findAllCharges(Integer invId);

    @Modifying
    @Transactional
    @Query("update SalesInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM SalesInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<SalesInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
