package com.tekfilo.account.ifrsschedule;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.util.AccountResponse;
import com.tekfilo.account.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/account/ifrsschedule")
public class IfrsScheduleController {

    @Autowired
    IfrsScheduleService ifrsScheduleService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<IfrsScheduleEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<IfrsScheduleEntity>>(ifrsScheduleService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<IfrsScheduleEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<IfrsScheduleEntity>(ifrsScheduleService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AccountResponse> save(@RequestBody IfrsScheduleDto ifrsScheduleDto) {
        AccountResponse response = new AccountResponse();
        try {
            IfrsScheduleEntity entity = ifrsScheduleService.save(ifrsScheduleDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AccountResponse> update(@RequestBody IfrsScheduleDto ifrsScheduleDto) {
        AccountResponse response = new AccountResponse();
        try {
            IfrsScheduleEntity entity = ifrsScheduleService.save(ifrsScheduleDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}/{operateby}")
    public ResponseEntity<AccountResponse> remove(@PathVariable("id") Integer id,
                                                  @PathVariable("operateby") Integer operateBy) {
        AccountResponse response = new AccountResponse();
        try {
            IfrsScheduleEntity entity = ifrsScheduleService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            ifrsScheduleService.remove(entity);
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

    @PutMapping("/removeall")
    public ResponseEntity<AccountResponse> remove(@RequestBody List<Integer> ids) {
        AccountResponse response = new AccountResponse();
        try {
            List<IfrsScheduleEntity> entities = ifrsScheduleService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            ifrsScheduleService.removeAll(entities);
            response.setId(null);
            response.setStatus(100);
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
            List<IfrsScheduleEntity> entities = ifrsScheduleService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            ifrsScheduleService.lock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<AccountResponse> unlock(@RequestBody List<Integer> ids) {
        AccountResponse response = new AccountResponse();
        try {
            List<IfrsScheduleEntity> entities = ifrsScheduleService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            ifrsScheduleService.unlock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }
}
