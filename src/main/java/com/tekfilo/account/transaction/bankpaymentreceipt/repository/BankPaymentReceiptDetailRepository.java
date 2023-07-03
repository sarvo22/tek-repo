package com.tekfilo.account.transaction.bankpaymentreceipt.repository;

import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankPaymentReceiptDetailRepository extends JpaRepository<BankPaymentReceiptDetailEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM BankPaymentReceiptDetailEntity where invId = :id and isDeleted = 0 ")
    List<BankPaymentReceiptDetailEntity> findAllByMainId(Integer id);
}
