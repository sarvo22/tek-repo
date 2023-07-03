package com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.repository;

import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MemoPurchaseInvoiceChargesRepository extends JpaRepository<MemoPurchaseInvoiceChargesEntity, Integer> {

    @Query("select c from MemoPurchaseInvoiceChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<MemoPurchaseInvoiceChargesEntity> findAllCharges(Integer invId);

    @Modifying
    @Transactional
    @Query("update MemoPurchaseInvoiceChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("select a from MemoPurchaseInvoiceChargesEntity a where a.isDeleted = 0 and a.invId in (:ids)")
    List<MemoPurchaseInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> ids);
}
