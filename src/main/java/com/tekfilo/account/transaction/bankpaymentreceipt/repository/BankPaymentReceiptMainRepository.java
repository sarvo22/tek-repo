package com.tekfilo.account.transaction.bankpaymentreceipt.repository;

import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BankPaymentReceiptMainRepository
        extends JpaRepository<BankPaymentReceiptMainEntity, Integer>, JpaSpecificationExecutor {

    @Modifying
    @Transactional
    @Query("update BankPaymentReceiptMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);
}
