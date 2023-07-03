package com.tekfilo.account.transaction.post;

import com.tekfilo.account.transaction.common.PartyRegisterDto;
import com.tekfilo.account.transaction.partyregister.PartyRegisterMainEntity;
import com.tekfilo.account.transaction.partyregister.PartyRegisterService;
import com.tekfilo.account.util.AccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/account/transaction/post")
public class PostingController {

    @Autowired
    PostingService postingService;

    @Autowired
    PartyRegisterService partyRegisterService;

    @PostMapping("/submit/{invoicetype}/{invoiceid}")
    public ResponseEntity<AccountResponse> doPostingAccounting(@PathVariable("invoicetype") String invoiceType,
                                                               @PathVariable("invoiceid") Integer invoiceId) {
        AccountResponse response = new AccountResponse();
        try {
            postingService.unPostAccounting(invoiceType, invoiceId);
            postingService.postAccounting(invoiceType, invoiceId);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Account entries are generated Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getMessage());
            log.error("Exception raised while posting service" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/{invoicetype}/{invoiceid}")
    public ResponseEntity<AccountResponse> unPostAccounting(@PathVariable("invoicetype") String invoiceType,
                                                            @PathVariable("invoiceid") Integer invoiceId) {
        AccountResponse response = new AccountResponse();
        try {
            postingService.unPostAccounting(invoiceType, invoiceId);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Account entries are removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getMessage());
            log.error("Exception raised while posting service" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteposting")
    public ResponseEntity<AccountResponse> unPostAccoutingList(@RequestBody PostingDeleteDto postingDeleteDto) {
        AccountResponse response = new AccountResponse();
        try {
            postingService.unPostAccounting(postingDeleteDto);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Account entries are removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getMessage());
            log.error("Exception raised while posting service" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/deletepostingbyvoucher/{type}/{id}")
    public ResponseEntity<AccountResponse> unPostAccoutingListByVoucher(@PathVariable("type") String invoiceType,
                                                                        @PathVariable("id") Integer id) {
        AccountResponse response = new AccountResponse();
        try {
            postingService.unPostAccounting(invoiceType, id);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Account entries are removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getMessage());
            log.error("Exception raised while posting service" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/postpartyaccountregister")
    public ResponseEntity<AccountResponse> postPartyAccountRegister(@RequestBody PartyRegisterDto partyRegisterDto) {
        AccountResponse response = new AccountResponse();
        try {
            partyRegisterService.createPartyAccountRegister(partyRegisterDto);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Party Statement generated Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getMessage());
            log.error("Exception raised while posting service" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/getcustomerpendinginvoicelist/{customerid}/{currency}")
    public ResponseEntity<List<PartyRegisterMainEntity>> getCustomerPendingInvoiceList(@PathVariable("customerid") Integer customerId,
                                                                                       @PathVariable("currency") String currency) {
        return new ResponseEntity<List<PartyRegisterMainEntity>>(this.partyRegisterService.findAllCustomerPendingList(customerId, currency), HttpStatus.OK);
    }

    @GetMapping("/getsupplierpendinginvoicelist/{supplierid}/{currency}")
    public ResponseEntity<List<PartyRegisterMainEntity>> getSupplierPendingInvoiceList(@PathVariable("supplierid") Integer supplierId,
                                                                                       @PathVariable("currency") String currency) {
        return new ResponseEntity<List<PartyRegisterMainEntity>>(this.partyRegisterService.findAllSupplierPendingList(supplierId, currency), HttpStatus.OK);
    }

    @GetMapping("/getcustomerpendinginvoicelistbycustomer/{customerid}")
    public ResponseEntity<List<PartyRegisterMainEntity>> getCustomerPendingInvoiceListByCustomer(@PathVariable("customerid") Integer customerId) {
        return new ResponseEntity<List<PartyRegisterMainEntity>>(this.partyRegisterService.findAllCustomerPendingList(customerId), HttpStatus.OK);
    }

    @GetMapping("/getsupplierpendinginvoicelistsupplier/{supplierid}")
    public ResponseEntity<List<PartyRegisterMainEntity>> getSupplierPendingInvoiceListBySupplier(@PathVariable("supplierid") Integer supplierId) {
        return new ResponseEntity<List<PartyRegisterMainEntity>>(this.partyRegisterService.findAllSupplierPendingList(supplierId), HttpStatus.OK);
    }

    @GetMapping("/getpostedlist/{accountid}")
    public ResponseEntity<List<PostingDetailEntity>> getPostedVoucherListByAccount(@PathVariable("accountid") Integer accountId) {
        return new ResponseEntity<List<PostingDetailEntity>>(this.postingService.findAllPostedVoucherListByAccount(accountId), HttpStatus.OK);
    }

}
