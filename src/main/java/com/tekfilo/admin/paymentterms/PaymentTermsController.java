package com.tekfilo.admin.paymentterms;

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
@RequestMapping("/admin/paymentterm")
public class PaymentTermsController {

    @Autowired
    PaymentTermsService paymentTermsService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<PaymentTermsEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection, @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<PaymentTermsEntity>>(paymentTermsService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<PaymentTermsEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<PaymentTermsEntity>(paymentTermsService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody PaymentTermsDto paymentTermsDto) {
        AdminResponse response = new AdminResponse();
        try {
            PaymentTermsEntity entity = paymentTermsService.save(paymentTermsDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Payment Terms {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AdminResponse> update(@RequestBody PaymentTermsDto paymentTermsDto) {
        AdminResponse response = new AdminResponse();
        try {
            PaymentTermsEntity entity = paymentTermsService.save(paymentTermsDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Payment Terms {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<AdminResponse> remove(@PathVariable("id") Integer id) {
        AdminResponse response = new AdminResponse();
        try {
            PaymentTermsEntity entity = paymentTermsService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            paymentTermsService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Payment Terms  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/removeall")
    public ResponseEntity<AdminResponse> remove(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<PaymentTermsEntity> entities = paymentTermsService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            paymentTermsService.removeAll(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Payment Terms {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<AdminResponse> lock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<PaymentTermsEntity> entities = paymentTermsService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            paymentTermsService.lock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Payment Terms {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<AdminResponse> unlock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<PaymentTermsEntity> entities = paymentTermsService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            paymentTermsService.unlock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Payment Terms {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

}
