package com.tekfilo.inventory.settlement.paymentpaid.repository;

import com.tekfilo.inventory.settlement.paymentpaid.entity.PaymentPaidMainEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface PaymentPaidMainRepository extends JpaRepository<PaymentPaidMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select p from PaymentPaidMainEntity p where p.isDeleted = 0 and companyId = :companyId")
    Page<PaymentPaidMainEntity> findAllPaymentPaid(Pageable pageable, Integer companyId);

    @Modifying
    @Transactional
    @Query("update PaymentPaidMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:id)")
    void deleteMainById(Integer id, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;


    @Query("select a from PaymentPaidMainEntity a where a.isDeleted = 0 and id in (:ids)")
    List<PaymentPaidMainEntity> findAllMainByMainIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("update PaymentPaidMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIdList(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update PaymentPaidMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);
}
