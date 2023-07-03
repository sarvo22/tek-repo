package com.tekfilo.account.transaction.jv;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.jv.dto.JVBreakupDto;
import com.tekfilo.account.transaction.jv.dto.JVDetailDto;
import com.tekfilo.account.transaction.jv.dto.JVMainDto;
import com.tekfilo.account.transaction.jv.dto.JVRequestPayload;
import com.tekfilo.account.transaction.jv.entity.JVBreakupEntity;
import com.tekfilo.account.transaction.jv.entity.JVDetailEntity;
import com.tekfilo.account.transaction.jv.entity.JVMainEntity;
import com.tekfilo.account.transaction.jv.service.JVService;
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
@RequestMapping("/account/transaction/jv")
public class JVController {

    @Autowired
    JVService jvService;

    @PostMapping("/main/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<JVMainEntity>> findAllMain(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<JVMainEntity>>(jvService.findAllMain(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/main/findbyid/{id}")
    public ResponseEntity<JVMainEntity> findMainById(@PathVariable("id") Integer id) {
        return new ResponseEntity<JVMainEntity>(jvService.findMainById(id), HttpStatus.OK);
    }

    @PostMapping("/main/save")
    public ResponseEntity<AccountResponse> saveMain(@RequestBody JVMainDto jvMainDto) {
        AccountResponse response = new AccountResponse();
        try {
            JVMainEntity entity = jvService.saveMain(jvMainDto);
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
    public ResponseEntity<AccountResponse> updateMain(@RequestBody JVMainDto jvMainDto) {
        AccountResponse response = new AccountResponse();
        try {
            JVMainEntity entity = jvService.saveMain(jvMainDto);
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
    public ResponseEntity<AccountResponse> updateDetail(@RequestBody JVDetailDto jvDetailDto) {
        AccountResponse response = new AccountResponse();
        try {
            JVDetailEntity entity = jvService.saveDetail(jvDetailDto);
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
    public ResponseEntity<AccountResponse> updateBreakup(@RequestBody JVBreakupDto bankPaymentReceiptBreakupDto) {
        AccountResponse response = new AccountResponse();
        try {
            JVBreakupEntity entity = jvService.saveBreakup(bankPaymentReceiptBreakupDto);
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
            JVMainEntity entity = jvService.findMainById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            jvService.removeMain(entity);
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
    public ResponseEntity<List<JVDetailEntity>> findDetailByMainId(@PathVariable("mainid") Integer mainId) {
        return new ResponseEntity<List<JVDetailEntity>>(this.jvService.findAllDetailByMainId(mainId), HttpStatus.OK);
    }

    @PostMapping("/saveall")
    public ResponseEntity<AccountResponse> saveall(@RequestBody JVRequestPayload jvRequestPayload) {
        AccountResponse response = new AccountResponse();
        try {
            JVMainEntity entity = jvService.saveAll(jvRequestPayload);
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
    public ResponseEntity<AccountResponse> saveDetail(@RequestBody JVDetailDto jvDetailDto) {
        AccountResponse response = new AccountResponse();
        try {

            JVDetailEntity entity = jvService.saveDetail(jvDetailDto);
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
    public ResponseEntity<AccountResponse> saveBreakup(@RequestBody JVBreakupDto bankPaymentReceiptBreakupDto){
        AccountResponse response = new AccountResponse();
        try {

            JVBreakupEntity entity = jvService.saveBreakup(bankPaymentReceiptBreakupDto);
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
            JVBreakupEntity bankPaymentReceiptBreakupEntity = this.jvService.findBreakupById(id);
            bankPaymentReceiptBreakupEntity.setModifiedBy(UserContext.getLoggedInUser());
            bankPaymentReceiptBreakupEntity.setIsDeleted(1);
            jvService.saveBreaup(bankPaymentReceiptBreakupEntity);
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
    public ResponseEntity<AccountResponse> removeDetail(@PathVariable("id") Integer id) {
        AccountResponse response = new AccountResponse();
        try {
            JVDetailEntity detailEntity = this.jvService.findDetailById(id);
            detailEntity.setModifiedBy(UserContext.getLoggedInUser());
            detailEntity.setIsDeleted(1);
            jvService.removeDetail(detailEntity);
            response.setId(detailEntity.getId());
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

    @PutMapping("/removeall")
    public ResponseEntity<AccountResponse> removeAll(@RequestBody List<Integer> ids) {
        AccountResponse response = new AccountResponse();
        try {
            Integer userId = UserContext.getLoggedInUser();
            List<JVMainEntity> mainEntities = jvService.findJVsByIds(ids);
            mainEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<JVDetailEntity> detailEntities = jvService.findAllDetailByMainIds(ids);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<JVBreakupEntity> breakupEntities = jvService.findJVsBreakupByDetailIds(
                    detailEntities.stream().map(JVDetailEntity::getId).collect(Collectors.toList())
            );
            breakupEntities.stream().forEach(e->{
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            jvService.removeAll(mainEntities, detailEntities,breakupEntities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/lock")
    public ResponseEntity<AccountResponse> lock(@RequestBody List<Integer> ids) {
        AccountResponse response = new AccountResponse();
        try {
            List<JVMainEntity> entities = jvService.findJVsByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            jvService.lock(entities);
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
            List<JVMainEntity> entities = jvService.findJVsByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            jvService.unlock(entities);
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

    @PutMapping("/reversal")
    public ResponseEntity<AccountResponse> reversal(@RequestBody List<Integer> ids) {
        AccountResponse response = new AccountResponse();
        try {
            jvService.reversal(ids);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_REVERSED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }
}
