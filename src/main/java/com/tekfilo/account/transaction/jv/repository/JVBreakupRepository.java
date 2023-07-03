package com.tekfilo.account.transaction.jv.repository;

import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptBreakupEntity;
import com.tekfilo.account.transaction.jv.entity.JVBreakupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JVBreakupRepository extends JpaRepository<JVBreakupEntity, Integer>, JpaSpecificationExecutor {
}
