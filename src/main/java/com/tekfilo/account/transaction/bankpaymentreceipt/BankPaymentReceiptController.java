package com.tekfilo.account.transaction.bankpaymentreceipt;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptBreakupDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptDetailDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankPaymentReceiptMainDto;
import com.tekfilo.account.transaction.bankpaymentreceipt.dto.BankRequestPayload;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptBreakupEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptDetailEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptMainEntity;
import com.tekfilo.account.transaction.bankpaymentreceipt.service.BankPaymentReceiptService;
import com.tekfilo.account.util.AccountResponse;
import com.tekfilo.account.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/account/transaction/bankpaymentreceipt")
public class BankPaymentReceiptController {

    @Autowired
    BankPaymentReceiptService bankPaymentReceiptService;

    @PostMapping("/main/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<BankPaymentReceiptMainEntity>> findAllMain(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<BankPaymentReceiptMainEntity>>(bankPaymentReceiptService.findAllMain(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/main/findbyid/{id}")
    public ResponseEntity<BankPaymentReceiptMainEntity> findMainById(@PathVariable("id") Integer id) {
        return new ResponseEntity<BankPaymentReceiptMainEntity>(bankPaymentReceiptService.findMainById(id), HttpStatus.OK);
    }

    @PostMapping("/main/save")
    public ResponseEntity<AccountResponse> saveMain(@RequestBody BankPaymentReceiptMainDto bankPaymentReceiptMainDto) {
        AccountResponse response = new AccountResponse();
        try {
            BankPaymentReceiptMainEntity entity = bankPaymentReceiptService.saveMain(bankPaymentReceiptMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/main/modify")
    public ResponseEntity<AccountResponse> updateMain(@RequestBody BankPaymentReceiptMainDto bankPaymentReceiptMainDto) {
        AccountResponse response = new AccountResponse();
        try {
            BankPaymentReceiptMainEntity entity = bankPaymentReceiptService.saveMain(bankPaymentReceiptMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/detail/modify")
    public ResponseEntity<AccountResponse> updateDetail(@RequestBody BankPaymentReceiptDetailDto detailDto) {
        AccountResponse response = new AccountResponse();
        try {
            BankPaymentReceiptDetailEntity entity = bankPaymentReceiptService.saveDetail(detailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/breakup/modify")
    public ResponseEntity<AccountResponse> updateBreakup(@RequestBody BankPaymentReceiptBreakupDto bankPaymentReceiptBreakupDto) {
        AccountResponse response = new AccountResponse();
        try {
            BankPaymentReceiptBreakupEntity entity = bankPaymentReceiptService.saveBreakup(bankPaymentReceiptBreakupDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/main/remove/{id}")
    public ResponseEntity<AccountResponse> removeMain(@PathVariable("id") Integer id) {
        AccountResponse response = new AccountResponse();
        try {
            BankPaymentReceiptMainEntity entity = bankPaymentReceiptService.findMainById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            bankPaymentReceiptService.removeMain(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying   {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/detail/findall/{mainid}")
    public ResponseEntity<List<BankPaymentReceiptDetailEntity>> findDetailByMainId(@PathVariable("mainid") Integer mainId){
        return new ResponseEntity<List<BankPaymentReceiptDetailEntity>>( this.bankPaymentReceiptService.findAllDetailByMainId(mainId),HttpStatus.OK);
    }

    @PostMapping("/saveall")
    public ResponseEntity<AccountResponse> saveall(@RequestBody BankRequestPayload bankRequestPayload){
        AccountResponse response = new AccountResponse();
        try {
            BankPaymentReceiptMainEntity entity = bankPaymentReceiptService.saveAll(bankRequestPayload);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/savedetail")
    public ResponseEntity<AccountResponse> saveDetail(@RequestBody BankPaymentReceiptDetailDto bankPaymentReceiptDetailDto){
        AccountResponse response = new AccountResponse();
        try {

            BankPaymentReceiptDetailEntity entity = bankPaymentReceiptService.saveDetail(bankPaymentReceiptDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/savebreakup")
    public ResponseEntity<AccountResponse> saveBreakup(@RequestBody BankPaymentReceiptBreakupDto bankPaymentReceiptBreakupDto){
        AccountResponse response = new AccountResponse();
        try {

            BankPaymentReceiptBreakupEntity entity = bankPaymentReceiptService.saveBreakup(bankPaymentReceiptBreakupDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/breakup/remove/{id}")
    public ResponseEntity<AccountResponse> removeBreakup(@PathVariable("id") Integer id){
        AccountResponse response = new AccountResponse();
        try {
            BankPaymentReceiptBreakupEntity bankPaymentReceiptBreakupEntity = this.bankPaymentReceiptService.findBreakupById(id);
            bankPaymentReceiptBreakupEntity.setModifiedBy(UserContext.getLoggedInUser());
            bankPaymentReceiptBreakupEntity.setIsDeleted(1);
            bankPaymentReceiptService.saveBreaup(bankPaymentReceiptBreakupEntity);
            response.setId(bankPaymentReceiptBreakupEntity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying   {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/remove/{id}")
    public ResponseEntity<AccountResponse> removeDetail(@PathVariable("id") Integer id){
        AccountResponse response = new AccountResponse();
        try {
            BankPaymentReceiptDetailEntity detailEntity = this.bankPaymentReceiptService.findDetailById(id);
            detailEntity.setModifiedBy(UserContext.getLoggedInUser());
            detailEntity.setIsDeleted(1);
            List<BankPaymentReceiptBreakupEntity> breakupEntities = this.bankPaymentReceiptService.findBankPaymentReceiptsBreakupByDetailIds(Arrays.asList(id));
            breakupEntities.stream().forEach(e-> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });
            bankPaymentReceiptService.removeDetail(detailEntity, breakupEntities);
            response.setId(detailEntity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying   {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<AccountResponse> removeAll(@RequestBody List<Integer> ids) {
        AccountResponse response = new AccountResponse();
        try {
            Integer userId = UserContext.getLoggedInUser();
            List<BankPaymentReceiptMainEntity> mainEntities = bankPaymentReceiptService.findBankPaymentReceiptsByIds(ids);
            mainEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<BankPaymentReceiptDetailEntity> detailEntities = bankPaymentReceiptService.findAllDetailByMainIds(ids);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<BankPaymentReceiptBreakupEntity> breakupEntities = bankPaymentReceiptService.findBankPaymentReceiptsBreakupByDetailIds(
                    detailEntities.stream().map(BankPaymentReceiptDetailEntity::getId).collect(Collectors.toList())
            );
            breakupEntities.stream().forEach(e->{
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            bankPaymentReceiptService.removeAll(mainEntities, detailEntities,breakupEntities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/lock")
    public ResponseEntity<AccountResponse> lock(@RequestBody List<Integer> ids) {
        AccountResponse response = new AccountResponse();
        try {
            List<BankPaymentReceiptMainEntity> entities = bankPaymentReceiptService.findBankPaymentReceiptsByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            bankPaymentReceiptService.lock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<AccountResponse> unlock(@RequestBody List<Integer> ids) {
        AccountResponse response = new AccountResponse();
        try {
            List<BankPaymentReceiptMainEntity> entities = bankPaymentReceiptService.findBankPaymentReceiptsByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            bankPaymentReceiptService.unlock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }
}
