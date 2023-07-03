package com.tekfilo.account.transaction.debitcreditnote;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteBreakupDto;
import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteDetailDto;
import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteMainDto;
import com.tekfilo.account.transaction.debitcreditnote.dto.DebitCreditNoteRequestPayload;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteBreakupEntity;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteDetailEntity;
import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteMainEntity;
import com.tekfilo.account.transaction.debitcreditnote.service.DebitCreditNoteService;
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
@RequestMapping("/account/transaction/debitcreditnote")
public class DebitCreditNoteController {

    @Autowired
    DebitCreditNoteService debitCrediteNoteService;

    @PostMapping("/main/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<DebitCreditNoteMainEntity>> findAllMain(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<DebitCreditNoteMainEntity>>(debitCrediteNoteService.findAllMain(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/main/findbyid/{id}")
    public ResponseEntity<DebitCreditNoteMainEntity> findMainById(@PathVariable("id") Integer id) {
        return new ResponseEntity<DebitCreditNoteMainEntity>(debitCrediteNoteService.findMainById(id), HttpStatus.OK);
    }

    @PostMapping("/main/save")
    public ResponseEntity<AccountResponse> saveMain(@RequestBody DebitCreditNoteMainDto debitCreditNoteMainDto) {
        AccountResponse response = new AccountResponse();
        try {
            DebitCreditNoteMainEntity entity = debitCrediteNoteService.saveMain(debitCreditNoteMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
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
    public ResponseEntity<AccountResponse> updateMain(@RequestBody DebitCreditNoteMainDto debitCreditNoteMainDto) {
        AccountResponse response = new AccountResponse();
        try {
            DebitCreditNoteMainEntity entity = debitCrediteNoteService.saveMain(debitCreditNoteMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
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
    public ResponseEntity<AccountResponse> updateDetail(@RequestBody DebitCreditNoteDetailDto detailDto) {
        AccountResponse response = new AccountResponse();
        try {
            DebitCreditNoteDetailEntity entity = debitCrediteNoteService.saveDetail(detailDto);
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
    public ResponseEntity<AccountResponse> updateBreakup(@RequestBody DebitCreditNoteBreakupDto debitCreditNoteBreakupDto) {
        AccountResponse response = new AccountResponse();
        try {
            DebitCreditNoteBreakupEntity entity = debitCrediteNoteService.saveBreakup(debitCreditNoteBreakupDto);
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
            DebitCreditNoteMainEntity entity = debitCrediteNoteService.findMainById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            debitCrediteNoteService.removeMain(entity);
            response.setId(entity.getId());
            response.setStatus(100);
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

    @GetMapping("/detail/findall/{mainid}")
    public ResponseEntity<List<DebitCreditNoteDetailEntity>> findDetailByMainId(@PathVariable("mainid") Integer mainId) {
        return new ResponseEntity<List<DebitCreditNoteDetailEntity>>(this.debitCrediteNoteService.findAllDetailByMainId(mainId), HttpStatus.OK);
    }

    @PostMapping("/saveall")
    public ResponseEntity<AccountResponse> saveAll(@RequestBody DebitCreditNoteRequestPayload debitCreditNoteRequestPayload) {
        AccountResponse response = new AccountResponse();
        try {
            DebitCreditNoteMainEntity entity = debitCrediteNoteService.saveAll(debitCreditNoteRequestPayload);
            response.setId(entity.getId());
            response.setStatus(100);
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
    public ResponseEntity<AccountResponse> saveDetail(@RequestBody DebitCreditNoteDetailDto debitCreditNoteDetailDto) {
        AccountResponse response = new AccountResponse();
        try {

            DebitCreditNoteDetailEntity entity = debitCrediteNoteService.saveDetail(debitCreditNoteDetailDto);
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
    public ResponseEntity<AccountResponse> saveBreakup(@RequestBody DebitCreditNoteBreakupDto debitCreditNoteBreakupDto) {
        AccountResponse response = new AccountResponse();
        try {
            DebitCreditNoteBreakupEntity entity = debitCrediteNoteService.saveBreakup(debitCreditNoteBreakupDto);
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
    public ResponseEntity<AccountResponse> removeBreakup(@PathVariable("id") Integer id) {
        AccountResponse response = new AccountResponse();
        try {
            DebitCreditNoteBreakupEntity debitCreditNoteBreakupEntity = this.debitCrediteNoteService.findBreakupById(id);
            debitCreditNoteBreakupEntity.setModifiedBy(UserContext.getLoggedInUser());
            debitCreditNoteBreakupEntity.setIsDeleted(1);
            debitCrediteNoteService.saveBreaup(debitCreditNoteBreakupEntity);
            response.setId(debitCreditNoteBreakupEntity.getId());
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
            DebitCreditNoteDetailEntity detailEntity = this.debitCrediteNoteService.findDetailById(id);
            detailEntity.setModifiedBy(UserContext.getLoggedInUser());
            detailEntity.setIsDeleted(1);
            List<DebitCreditNoteBreakupEntity> breakupEntities = this.debitCrediteNoteService.findDebitCreditNotesBreakupByDetailIds(Arrays.asList(id));
            breakupEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });
            debitCrediteNoteService.removeDetail(detailEntity, breakupEntities);
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
            List<DebitCreditNoteMainEntity> mainEntities = debitCrediteNoteService.findDebitCreditNotesByIds(ids);
            mainEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<DebitCreditNoteDetailEntity> detailEntities = debitCrediteNoteService.findAllDetailByMainIds(ids);
            detailEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<DebitCreditNoteBreakupEntity> breakupEntities = debitCrediteNoteService.findDebitCreditNotesBreakupByDetailIds(
                    detailEntities.stream().map(DebitCreditNoteDetailEntity::getId).collect(Collectors.toList())
            );
            breakupEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            debitCrediteNoteService.removeAll(mainEntities, detailEntities, breakupEntities);
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
            List<DebitCreditNoteMainEntity> entities = debitCrediteNoteService.findDebitCreditNotesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            debitCrediteNoteService.lock(entities);
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
            List<DebitCreditNoteMainEntity> entities = debitCrediteNoteService.findDebitCreditNotesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            debitCrediteNoteService.unlock(entities);
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