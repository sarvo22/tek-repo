package com.tekfilo.admin.factory;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.factory.dto.*;
import com.tekfilo.admin.factory.entity.*;
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
@RequestMapping("/admin/factory")
public class FactoryController {


    @Autowired
    FactoryService factoryService;


    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<FactoryEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<FactoryEntity>>(factoryService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbykey/{key}")
    public ResponseEntity<List<FactoryEntity>> findByKey(@PathVariable("key") String key) {
        return new ResponseEntity<List<FactoryEntity>>(this.factoryService.getFactoryList(key), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<FactoryEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<FactoryEntity>(factoryService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody FactoryDto factoryDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryEntity entity = factoryService.save(factoryDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AdminResponse> update(@RequestBody FactoryDto factoryDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryEntity entity = factoryService.save(factoryDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}/{operateby}")
    public ResponseEntity<AdminResponse> remove(@PathVariable("id") Integer id,
                                                @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryEntity entity = factoryService.findById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            factoryService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/search/address/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<FactoryAddressEntity>> findAllFactoryAddressFactoryAddress(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<FactoryAddressEntity>>(factoryService.findAllFactoryAddress(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/address/findbyid/{id}")
    public ResponseEntity<FactoryAddressEntity> findFactoryAddressById(@PathVariable("id") Integer id) {
        return new ResponseEntity<FactoryAddressEntity>(factoryService.findFactoryAddressById(id), HttpStatus.OK);
    }

    @GetMapping("/address/findbyfactoryid/{factoryid}")
    public ResponseEntity<List<FactoryAddressEntity>> findAddressByFactoryId(@PathVariable("factoryid") Integer factoryId) {
        return new ResponseEntity<List<FactoryAddressEntity>>(factoryService.findAddressByFactoryId(factoryId), HttpStatus.OK);

    }

    @PostMapping("/save/address")
    public ResponseEntity<AdminResponse> saveAddress(@RequestBody FactoryAddressDto factoryAddressDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryAddressEntity entity = factoryService.saveFactoryAddress(factoryAddressDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify/address")
    public ResponseEntity<AdminResponse> updateAddress(@RequestBody FactoryAddressDto factoryAddressDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryAddressEntity entity = factoryService.saveFactoryAddress(factoryAddressDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/address/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeAddress(@PathVariable("id") Integer id,
                                                       @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryAddressEntity entity = factoryService.findFactoryAddressById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            factoryService.removeFactoryAddress(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/search/contact/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<FactoryContactEntity>> findAllFactoryContact(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<FactoryContactEntity>>(factoryService.findAllFactoryContact(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/contact/findbyid/{id}")
    public ResponseEntity<FactoryContactEntity> findFactoryContactById(@PathVariable("id") Integer id) {
        return new ResponseEntity<FactoryContactEntity>(factoryService.findFactoryContactById(id), HttpStatus.OK);
    }

    @GetMapping("/contact/findbyfactoryid/{factoryid}")
    public ResponseEntity<List<FactoryContactEntity>> findContactsByFactoryId(@PathVariable("factoryid") Integer factoryId) {
        return new ResponseEntity<List<FactoryContactEntity>>(factoryService.findContactsByFactoryId(factoryId), HttpStatus.OK);
    }

    @PostMapping("/save/contact")
    public ResponseEntity<AdminResponse> saveContact(@RequestBody FactoryContactDto factoryContactDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryContactEntity entity = factoryService.saveFactoryContact(factoryContactDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify/contact")
    public ResponseEntity<AdminResponse> updateContact(@RequestBody FactoryContactDto factoryContactDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryContactEntity entity = factoryService.saveFactoryContact(factoryContactDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/contact/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeContact(@PathVariable("id") Integer id,
                                                       @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryContactEntity entity = factoryService.findFactoryContactById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            factoryService.removeFactoryContact(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    // Factory Limit

    @GetMapping("/search/limit/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<FactoryLimitEntity>> findAllFactoryLimit(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<FactoryLimitEntity>>(factoryService.findAllFactoryLimit(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/limit/findbyid/{id}")
    public ResponseEntity<FactoryLimitEntity> findFactoryLimitById(@PathVariable("id") Integer id) {
        return new ResponseEntity<FactoryLimitEntity>(factoryService.findFactoryLimitById(id), HttpStatus.OK);
    }

    @GetMapping("/limit/findbyfactoryid/{factoryid}")
    public ResponseEntity<List<FactoryLimitEntity>> findLimitsByFactoryId(@PathVariable("factoryid") Integer factoryId) {
        return new ResponseEntity<List<FactoryLimitEntity>>(factoryService.findLimitsByFactoryId(factoryId), HttpStatus.OK);

    }

    @PostMapping("/save/limit")
    public ResponseEntity<AdminResponse> saveLimit(@RequestBody FactoryLimitDto factoryLimitDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryLimitEntity entity = factoryService.saveFactoryLimit(factoryLimitDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify/limit")
    public ResponseEntity<AdminResponse> updateLimit(@RequestBody FactoryLimitDto factoryLimitDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryLimitEntity entity = factoryService.saveFactoryLimit(factoryLimitDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/limit/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeLimit(@PathVariable("id") Integer id,
                                                     @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryLimitEntity entity = factoryService.findFactoryLimitById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            factoryService.removeFactoryLimit(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    // Factory Document

    @GetMapping("/search/document/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<FactoryDocumentEntity>> findAllFactoryDocument(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<FactoryDocumentEntity>>(factoryService.findAllFactoryDocument(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/document/findbyid/{id}")
    public ResponseEntity<FactoryDocumentEntity> findFactoryDocumentById(@PathVariable("id") Integer id) {
        return new ResponseEntity<FactoryDocumentEntity>(factoryService.findFactoryDocumentById(id), HttpStatus.OK);
    }

    @GetMapping("/document/finddocumentbyfactoryid/{factoryid}")
    public ResponseEntity<List<FactoryDocumentEntity>> findDocumentsByFactoryId(@PathVariable("factoryid") Integer factoryId) {
        return new ResponseEntity<List<FactoryDocumentEntity>>(factoryService.findDocumentsByFactoryId(factoryId), HttpStatus.OK);
    }

    @PostMapping("/save/document")
    public ResponseEntity<AdminResponse> saveDocument(@RequestBody FactoryDocumentDto factoryDocumentDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryDocumentEntity entity = factoryService.saveFactoryDocument(factoryDocumentDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify/document")
    public ResponseEntity<AdminResponse> updateDocument(@RequestBody FactoryDocumentDto factoryDocumentDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryDocumentEntity entity = factoryService.saveFactoryDocument(factoryDocumentDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/document/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeDocument(@PathVariable("id") Integer id,
                                                        @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryDocumentEntity entity = factoryService.findFactoryDocumentById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            factoryService.removeFactoryDocument(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<AdminResponse> removeAll(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            factoryService.removeAll(ids);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<AdminResponse> lock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<FactoryEntity> entities = factoryService.findAllEntitiesByIds(ids);
            factoryService.lock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity Group {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<AdminResponse> unlock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<FactoryEntity> entities = factoryService.findAllEntitiesByIds(ids);
            factoryService.unlock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity Group {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }
}
