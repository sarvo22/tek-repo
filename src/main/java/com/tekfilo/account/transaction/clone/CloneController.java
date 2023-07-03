package com.tekfilo.account.transaction.clone;

import com.tekfilo.account.accmaster.AccMasterService;
import com.tekfilo.account.transaction.bankpaymentreceipt.service.BankPaymentReceiptService;
import com.tekfilo.account.transaction.cashpaymentreceipt.service.CashPaymentReceiptService;
import com.tekfilo.account.transaction.debitcreditnote.service.DebitCreditNoteService;
import com.tekfilo.account.transaction.jv.service.JVService;
import com.tekfilo.account.util.AccountResponse;
import com.tekfilo.account.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account/clone")
public class CloneController {

    @Autowired
    AccMasterService accMasterService;

    @Autowired
    BankPaymentReceiptService bankPaymentReceiptService;

    @Autowired
    CashPaymentReceiptService cashPaymentReceiptService;

    @Autowired
    DebitCreditNoteService debitCreditNoteService;

    @Autowired
    JVService jvService;

    @PostMapping("/accountmaster")
    public ResponseEntity<AccountResponse> copyAccountMaster(@RequestBody ClonePayload clonePayload) {
        AccountResponse response = new AccountResponse();
        try {
            accMasterService.cloneAccountMaster(clonePayload);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_CLONE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/banktransaction")
    public ResponseEntity<AccountResponse> copyBankTransaction(@RequestBody ClonePayload clonePayload) {
        AccountResponse response = new AccountResponse();
        try {
            bankPaymentReceiptService.clone(clonePayload);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_CLONE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/cashtransaction")
    public ResponseEntity<AccountResponse> copyCashTransaction(@RequestBody ClonePayload clonePayload) {
        AccountResponse response = new AccountResponse();
        try {
            cashPaymentReceiptService.clone(clonePayload);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_CLONE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/debitcreditnotetransaction")
    public ResponseEntity<AccountResponse> copyDebitCreditNoteTransaction(@RequestBody ClonePayload clonePayload) {
        AccountResponse response = new AccountResponse();
        try {
            debitCreditNoteService.clone(clonePayload);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_CLONE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/jvtransaction")
    public ResponseEntity<AccountResponse> copyJVTransaction(@RequestBody ClonePayload clonePayload) {
        AccountResponse response = new AccountResponse();
        try {
            jvService.clone(clonePayload);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_CLONE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }
}
