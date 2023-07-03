package com.tekfilo.inventory.settlement.paymentpaid.repository;

import com.tekfilo.inventory.settlement.paymentpaid.entity.PaymentPaidDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PaymentPaidDetailRepository extends JpaRepository<PaymentPaidDetailEntity, Integer> {

    @Query("select d from PaymentPaidDetailEntity d where d.isDeleted = 0 and d.paymentId = :id")
    List<PaymentPaidDetailEntity> findAllByMainId(Integer id);

    @Query("FROM PaymentPaidDetailEntity where isDeleted = 0 and invId = :referenceInvoiceId and invType = :referenceInvoiceType")
    List<PaymentPaidDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    @Query("select a from PaymentPaidDetailEntity a where a.isDeleted = 0 and a.paymentId in (:ids)")
    List<PaymentPaidDetailEntity> findAllDetailByMainIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("update PaymentPaidDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

}
