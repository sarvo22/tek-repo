package com.tekfilo.inventory.settlement.paymentreceived.repository;

import com.tekfilo.inventory.settlement.paymentreceived.entity.PaymentReceivedDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PaymentReceivedDetailRepository extends JpaRepository<PaymentReceivedDetailEntity, Integer> {

    @Query("select d from PaymentReceivedDetailEntity d where d.isDeleted = 0 and d.paymentId = :id")
    List<PaymentReceivedDetailEntity> findAllByMainId(Integer id);

    @Query("FROM PaymentReceivedDetailEntity where isDeleted = 0 and invId = :referenceInvoiceId and invType = :referenceInvoiceType")
    List<PaymentReceivedDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    @Query("select a from PaymentReceivedDetailEntity a where a.isDeleted = 0 and a.paymentId in (:ids)")
    List<PaymentReceivedDetailEntity> findAllDetailByMainIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("update PaymentReceivedDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
