package com.tekfilo.account.transaction.cashpaymentreceipt;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashPaymentReceiptBreakupDto;
import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashPaymentReceiptDetailDto;
import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashPaymentReceiptMainDto;
import com.tekfilo.account.transaction.cashpaymentreceipt.dto.CashRequestPayload;
import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptBreakupEntity;
import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptDetailEntity;
import com.tekfilo.account.transaction.cashpaymentreceipt.entity.CashPaymentReceiptMainEntity;
import com.tekfilo.account.transaction.cashpaymentreceipt.service.CashPaymentReceiptService;
import com.tekfilo.account.util.AccountResponse;
import com.tekfilo.account.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/account/transaction/cashpaymentreceipt")
public class CashPaymentReceiptController {

    @Autowired
    CashPaymentReceiptService cashPaymentReceiptService;

    @PostMapping("/main/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<CashPaymentReceiptMainEntity>> findAllMain(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<CashPaymentReceiptMainEntity>>(cashPaymentReceiptService.findAllMain(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/main/findbyid/{id}")
    public ResponseEntity<CashPaymentReceiptMainEntity> findMainById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CashPaymentReceiptMainEntity>(cashPaymentReceiptService.findMainById(id), HttpStatus.OK);
    }

    @PostMapping("/main/save")
    public ResponseEntity<AccountResponse> saveMain(@RequestBody CashPaymentReceiptMainDto cashPaymentReceiptMainDto) {
        AccountResponse response = new AccountResponse();
        try {
            CashPaymentReceiptMainEntity entity = cashPaymentReceiptService.saveMain(cashPaymentReceiptMainDto);
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

    @PutMapping("/main/modify")
    public ResponseEntity<AccountResponse> updateMain(@RequestBody CashPaymentReceiptMainDto cashPaymentReceiptMainDto) {
        AccountResponse response = new AccountResponse();
        try {
            CashPaymentReceiptMainEntity entity = cashPaymentReceiptService.saveMain(cashPaymentReceiptMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/detail/modify")
    public ResponseEntity<AccountResponse> updateDetail(@RequestBody CashPaymentReceiptDetailDto detailDto) {
        AccountResponse response = new AccountResponse();
        try {
            CashPaymentReceiptDetailEntity entity = cashPaymentReceiptService.saveDetail(detailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/breakup/modify")
    public ResponseEntity<AccountResponse> updateBreakup(@RequestBody CashPaymentReceiptBreakupDto cashPaymentReceiptBreakupDto) {
        AccountResponse response = new AccountResponse();
        try {
            CashPaymentReceiptBreakupEntity entity = cashPaymentReceiptService.saveBreakup(cashPaymentReceiptBreakupDto);
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
            CashPaymentReceiptMainEntity entity = cashPaymentReceiptService.findMainById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            cashPaymentReceiptService.removeMain(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/detail/findall/{mainid}")
    public ResponseEntity<List<CashPaymentReceiptDetailEntity>> findDetailByMainId(@PathVariable("mainid") Integer mainId){
        return new ResponseEntity<List<CashPaymentReceiptDetailEntity>>( this.cashPaymentReceiptService.findAllDetailByMainId(mainId),HttpStatus.OK);
    }

    @PostMapping("/saveall")
    public ResponseEntity<AccountResponse> saveall(@RequestBody CashRequestPayload cashRequestPayload){
        AccountResponse response = new AccountResponse();
        try {
            CashPaymentReceiptMainEntity entity = cashPaymentReceiptService.saveAll(cashRequestPayload);
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
    public ResponseEntity<AccountResponse> saveDetail(@RequestBody CashPaymentReceiptDetailDto cashPaymentReceiptDetailDto){
        AccountResponse response = new AccountResponse();
        try {

            CashPaymentReceiptDetailEntity entity = cashPaymentReceiptService.saveDetail(cashPaymentReceiptDetailDto);
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
    public ResponseEntity<AccountResponse> saveBreakup(@RequestBody CashPaymentReceiptBreakupDto cashPaymentReceiptBreakupDto){
        AccountResponse response = new AccountResponse();
        try {

            CashPaymentReceiptBreakupEntity entity = cashPaymentReceiptService.saveBreakup(cashPaymentReceiptBreakupDto);
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
            CashPaymentReceiptBreakupEntity cashPaymentReceiptBreakupEntity = this.cashPaymentReceiptService.findBreakupById(id);
            cashPaymentReceiptBreakupEntity.setModifiedBy(UserContext.getLoggedInUser());
            cashPaymentReceiptBreakupEntity.setIsDeleted(1);
            cashPaymentReceiptService.saveBreaup(cashPaymentReceiptBreakupEntity);
            response.setId(cashPaymentReceiptBreakupEntity.getId());
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
            CashPaymentReceiptDetailEntity detailEntity = this.cashPaymentReceiptService.findDetailById(id);
            detailEntity.setModifiedBy(UserContext.getLoggedInUser());
            detailEntity.setIsDeleted(1);
            cashPaymentReceiptService.removeDetail(detailEntity);
            response.setId(detailEntity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }
    @PutMapping("/removeall")
    public ResponseEntity<AccountResponse> removeAll(@RequestBody List<Integer> ids) {
        AccountResponse response = new AccountResponse();
        try {
            Integer userId = UserContext.getLoggedInUser();
            List<CashPaymentReceiptMainEntity> mainEntities = cashPaymentReceiptService.findCashPaymentReceiptsByIds(ids);
            mainEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<CashPaymentReceiptDetailEntity> detailEntities = cashPaymentReceiptService.findAllDetailByMainIds(ids);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<CashPaymentReceiptBreakupEntity> breakupEntities = cashPaymentReceiptService.findCashPaymentReceiptsBreakupByDetailIds(
                    detailEntities.stream().map(CashPaymentReceiptDetailEntity::getId).collect(Collectors.toList())
            );
            breakupEntities.stream().forEach(e->{
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            cashPaymentReceiptService.removeAll(mainEntities, detailEntities,breakupEntities);
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
            List<CashPaymentReceiptMainEntity> entities = cashPaymentReceiptService.findCashPaymentReceiptsByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            cashPaymentReceiptService.lock(entities);
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
            List<CashPaymentReceiptMainEntity> entities = cashPaymentReceiptService.findCashPaymentReceiptsByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            cashPaymentReceiptService.unlock(entities);
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
