package com.tekfilo.inventory.settlement.paymentreceived.repository;

import com.tekfilo.inventory.settlement.paymentreceived.entity.PaymentReceivedMainEntity;
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
public interface PaymentReceivedMainRepository extends JpaRepository<PaymentReceivedMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select p from PaymentReceivedMainEntity p where p.isDeleted = 0 and companyId = :companyId")
    Page<PaymentReceivedMainEntity> findAllPaymentReceived(Pageable pageable, Integer companyId);

    @Query("select a from PaymentReceivedMainEntity a where a.isDeleted = 0 and id in (:ids)")
    List<PaymentReceivedMainEntity> findAllMainByMainIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("update PaymentReceivedMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIdList(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update PaymentReceivedMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);
}
