package com.tekfilo.account.transaction.bankpaymentreceipt.repository;

import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptBreakupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankPaymentReceiptBreakupRepository extends JpaRepository<BankPaymentReceiptBreakupEntity, Integer>, JpaSpecificationExecutor {
}
