package com.tekfilo.admin.process;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.util.AdminResponse;
import com.tekfilo.admin.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    ProcessService processService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<ProcessEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<ProcessEntity>>(processService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<ProcessEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<ProcessEntity>(processService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody ProcessEntity processEntity) {
        AdminResponse response = new AdminResponse();
        try {
            ProcessEntity entity = processService.save(processEntity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_SUCCESS);
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving " + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AdminResponse> update(@RequestBody ProcessEntity processEntity) {
        AdminResponse response = new AdminResponse();
        try {
            ProcessEntity entity = processService.save(processEntity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_UPDATED);
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<AdminResponse> remove(@PathVariable("id") Integer id) {
        AdminResponse response = new AdminResponse();
        try {
            ProcessEntity entity = processService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            processService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_DELETED);
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/removeall")
    public ResponseEntity<AdminResponse> remove(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<ProcessEntity> entities = processService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            processService.removeAll(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_DELETED);
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<AdminResponse> lock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<ProcessEntity> entities = processService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            processService.lock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_LOCKED);
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<AdminResponse> unlock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<ProcessEntity> entities = processService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            processService.unlock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_UNLOCKED);
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }
}
