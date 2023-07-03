package com.tekfilo.account.upload;

import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptMainEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.repository.BankPaymentReceiptMainRepository;
import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptMainEntity;
import com.tekfilo.account.transaction.cashpaymentreceipt.repository.CashPaymentReceiptMainRepository;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteMainEntity;
import com.tekfilo.account.transaction.debitcreditnote.repository.DebitCreditNoteMainRepository;
import com.tekfilo.account.transaction.jv.entity.JVMainEntity;
import com.tekfilo.account.transaction.jv.repository.JVMainRepository;
import com.tekfilo.account.util.UploadTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UploadService {

    private static final CharSequence DELIMIER = ",";

    @Autowired
    BankPaymentReceiptMainRepository bankPaymentReceiptMainRepository;

    @Autowired
    CashPaymentReceiptMainRepository cashPaymentReceiptMainRepository;

    @Autowired
    JVMainRepository jvMainRepository;

    @Autowired
    DebitCreditNoteMainRepository debitCreditNoteMainRepository;

    public void updateUploadedDocumentUrls(DocumentDto documentDto) throws Exception {
        String documentUrl = "";
        switch (UploadTypes.valueOf(documentDto.getUploadModule().toUpperCase())) {
            case BANK_PAY_REC:
                BankPaymentReceiptMainEntity bankPaymentReceiptMainEntity = this.bankPaymentReceiptMainRepository.findById(documentDto.getInvoiceId()).orElse(new BankPaymentReceiptMainEntity());
                documentUrl = bankPaymentReceiptMainEntity.getDocumentUrl();
                this.bankPaymentReceiptMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case CASH_PAY_REC:
                CashPaymentReceiptMainEntity cashPaymentReceiptMainEntity = this.cashPaymentReceiptMainRepository.findById(documentDto.getInvoiceId()).orElse(new CashPaymentReceiptMainEntity());
                documentUrl = cashPaymentReceiptMainEntity.getDocumentUrl();
                this.cashPaymentReceiptMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case JV:
                JVMainEntity jvMainEntity = this.jvMainRepository.findById(documentDto.getInvoiceId()).orElse(new JVMainEntity());
                documentUrl = jvMainEntity.getDocumentUrl();
                this.jvMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            case DEBIT_CREDIT:
                DebitCreditNoteMainEntity debitCreditNoteMainEntity = this.debitCreditNoteMainRepository.findById(documentDto.getInvoiceId()).orElse(new DebitCreditNoteMainEntity());
                documentUrl = debitCreditNoteMainEntity.getDocumentUrl();
                this.debitCreditNoteMainRepository.updateDocumentUrl(documentDto.getInvoiceId(), getDocumentUrl(documentUrl, documentDto.getDocumentUrls()));
                break;
            default:
                break;
        }
    }

    private String getDocumentUrl(String dbDocumentUrl, String newDocumentUrls) {
        if (dbDocumentUrl == null) {
            return newDocumentUrls;
        } else if (dbDocumentUrl == "null") {
            return newDocumentUrls;
        } else {
            return String.join(DELIMIER, dbDocumentUrl, newDocumentUrls);
        }
    }
}
