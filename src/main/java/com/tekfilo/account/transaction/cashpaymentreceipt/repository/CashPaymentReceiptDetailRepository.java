package com.tekfilo.account.transaction.cashpaymentreceipt.repository;

import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashPaymentReceiptDetailRepository extends JpaRepository<CashPaymentReceiptDetailEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM CashPaymentReceiptDetailEntity where invId = :id and isDeleted = 0 ")
    List<CashPaymentReceiptDetailEntity> findAllByMainId(Integer id);
}
