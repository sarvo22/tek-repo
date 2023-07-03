package com.tekfilo.account.transaction.cashpaymentreceipt.repository;

import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptBreakupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CashPaymentReceiptBreakupRepository extends JpaRepository<CashPaymentReceiptBreakupEntity, Integer>, JpaSpecificationExecutor {
}
