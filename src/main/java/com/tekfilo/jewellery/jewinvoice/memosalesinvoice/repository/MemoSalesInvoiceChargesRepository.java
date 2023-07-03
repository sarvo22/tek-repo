package com.tekfilo.jewellery.jewinvoice.memosalesinvoice.repository;

import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MemoSalesInvoiceChargesRepository extends JpaRepository<MemoSalesInvoiceChargesEntity, Integer> {

    @Query("select c from MemoSalesInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<MemoSalesInvoiceChargesEntity> findAllCharges(Integer invId);

    @Modifying
    @Transactional
    @Query("update MemoSalesInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM MemoSalesInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<MemoSalesInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
