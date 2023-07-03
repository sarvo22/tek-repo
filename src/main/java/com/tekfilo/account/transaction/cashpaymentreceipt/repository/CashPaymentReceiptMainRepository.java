package com.tekfilo.account.transaction.cashpaymentreceipt.repository;

import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CashPaymentReceiptMainRepository
        extends JpaRepository<CashPaymentReceiptMainEntity, Integer>, JpaSpecificationExecutor {

    @Modifying
    @Transactional
    @Query("update CashPaymentReceiptMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);
}
