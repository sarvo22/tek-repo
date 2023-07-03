package com.tekfilo.account.transaction.debitcreditnote.repository;

import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptBreakupEntity;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteBreakupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitCreditNoteBreakupRepository extends JpaRepository<DebitCreditNoteBreakupEntity, Integer>, JpaSpecificationExecutor {
}
