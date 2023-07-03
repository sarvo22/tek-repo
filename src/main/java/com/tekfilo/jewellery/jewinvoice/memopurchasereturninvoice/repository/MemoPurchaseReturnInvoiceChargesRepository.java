package com.tekfilo.jewellery.jewinvoice.memopurchasereturninvoice.repository;

import com.tekfilo.jewellery.jewinvoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MemoPurchaseReturnInvoiceChargesRepository extends JpaRepository<MemoPurchaseReturnInvoiceChargesEntity, Integer> {

    @Query("select c from MemoPurchaseReturnInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<MemoPurchaseReturnInvoiceChargesEntity> findAllCharges(Integer invId);


    @Modifying
    @Transactional
    @Query("update MemoPurchaseReturnInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM MemoPurchaseReturnInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<MemoPurchaseReturnInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
