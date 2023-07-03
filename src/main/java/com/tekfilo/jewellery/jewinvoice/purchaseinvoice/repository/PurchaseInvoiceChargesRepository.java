package com.tekfilo.jewellery.jewinvoice.purchaseinvoice.repository;

import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PurchaseInvoiceChargesRepository extends JpaRepository<PurchaseInvoiceChargesEntity, Integer> {

    @Query("select c from PurchaseInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<PurchaseInvoiceChargesEntity> findAllCharges(Integer invId);


    @Modifying
    @Transactional
    @Query("update PurchaseInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM PurchaseInvoiceChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<PurchaseInvoiceChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);
}
