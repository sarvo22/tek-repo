package com.tekfilo.admin.supplier;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.supplier.dto.*;
import com.tekfilo.admin.supplier.entity.*;
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
@RequestMapping("/admin/supplier")
public class SupplierController {


    @Autowired
    SupplierService supplierService;


    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<SupplierEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<SupplierEntity>>(supplierService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbykey/{key}")
    public ResponseEntity<List<SupplierEntity>> findByKey(@PathVariable("key") String key) {
        return new ResponseEntity<List<SupplierEntity>>(this.supplierService.getSupplierList(key), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<SupplierEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SupplierEntity>(supplierService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody SupplierDto supplierDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierEntity entity = supplierService.save(supplierDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AdminResponse> update(@RequestBody SupplierDto supplierDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierEntity entity = supplierService.save(supplierDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}/{operateby}")
    public ResponseEntity<AdminResponse> remove(@PathVariable("id") Integer id,
                                                @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierEntity entity = supplierService.findById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            supplierService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/search/address/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<SupplierAddressEntity>> findAllSupplierAddressSupplierAddress(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<SupplierAddressEntity>>(supplierService.findAllSupplierAddress(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/address/findbyid/{id}")
    public ResponseEntity<SupplierAddressEntity> findSupplierAddressById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SupplierAddressEntity>(supplierService.findSupplierAddressById(id), HttpStatus.OK);
    }

    @GetMapping("/address/findbysupplierid/{supplierid}")
    public ResponseEntity<List<SupplierAddressEntity>> findAddressBySupplierId(@PathVariable("supplierid") Integer supplierId) {
        return new ResponseEntity<List<SupplierAddressEntity>>(supplierService.findAddressBySupplierId(supplierId), HttpStatus.OK);

    }

    @PostMapping("/save/address")
    public ResponseEntity<AdminResponse> saveAddress(@RequestBody SupplierAddressDto supplierAddressDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierAddressEntity entity = supplierService.saveSupplierAddress(supplierAddressDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify/address")
    public ResponseEntity<AdminResponse> updateAddress(@RequestBody SupplierAddressDto supplierAddressDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierAddressEntity entity = supplierService.saveSupplierAddress(supplierAddressDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/address/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeAddress(@PathVariable("id") Integer id,
                                                       @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierAddressEntity entity = supplierService.findSupplierAddressById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            supplierService.removeSupplierAddress(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/search/contact/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<SupplierContactEntity>> findAllSupplierContact(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<SupplierContactEntity>>(supplierService.findAllSupplierContact(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/contact/findbyid/{id}")
    public ResponseEntity<SupplierContactEntity> findSupplierContactById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SupplierContactEntity>(supplierService.findSupplierContactById(id), HttpStatus.OK);
    }

    @GetMapping("/contact/findbysupplierid/{supplierid}")
    public ResponseEntity<List<SupplierContactEntity>> findContactsBySupplierId(@PathVariable("supplierid") Integer supplierId) {
        return new ResponseEntity<List<SupplierContactEntity>>(supplierService.findContactsBySupplierId(supplierId), HttpStatus.OK);
    }

    @PostMapping("/save/contact")
    public ResponseEntity<AdminResponse> saveContact(@RequestBody SupplierContactDto supplierContactDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierContactEntity entity = supplierService.saveSupplierContact(supplierContactDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify/contact")
    public ResponseEntity<AdminResponse> updateContact(@RequestBody SupplierContactDto supplierContactDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierContactEntity entity = supplierService.saveSupplierContact(supplierContactDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/contact/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeContact(@PathVariable("id") Integer id,
                                                       @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierContactEntity entity = supplierService.findSupplierContactById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            supplierService.removeSupplierContact(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    // Supplier Limit

    @GetMapping("/search/limit/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<SupplierLimitEntity>> findAllSupplierLimit(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<SupplierLimitEntity>>(supplierService.findAllSupplierLimit(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/limit/findbyid/{id}")
    public ResponseEntity<SupplierLimitEntity> findSupplierLimitById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SupplierLimitEntity>(supplierService.findSupplierLimitById(id), HttpStatus.OK);
    }

    @GetMapping("/limit/findbysupplierid/{supplierid}")
    public ResponseEntity<List<SupplierLimitEntity>> findLimitsBySupplierId(@PathVariable("supplierid") Integer supplierId) {
        return new ResponseEntity<List<SupplierLimitEntity>>(supplierService.findLimitsBySupplierId(supplierId), HttpStatus.OK);

    }

    @PostMapping("/save/limit")
    public ResponseEntity<AdminResponse> saveLimit(@RequestBody SupplierLimitDto supplierLimitDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierLimitEntity entity = supplierService.saveSupplierLimit(supplierLimitDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify/limit")
    public ResponseEntity<AdminResponse> updateLimit(@RequestBody SupplierLimitDto supplierLimitDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierLimitEntity entity = supplierService.saveSupplierLimit(supplierLimitDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/limit/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeLimit(@PathVariable("id") Integer id,
                                                     @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierLimitEntity entity = supplierService.findSupplierLimitById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            supplierService.removeSupplierLimit(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    // Supplier Document

    @GetMapping("/search/document/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<SupplierDocumentEntity>> findAllSupplierDocument(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<SupplierDocumentEntity>>(supplierService.findAllSupplierDocument(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/document/findbyid/{id}")
    public ResponseEntity<SupplierDocumentEntity> findSupplierDocumentById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SupplierDocumentEntity>(supplierService.findSupplierDocumentById(id), HttpStatus.OK);
    }

    @GetMapping("/document/finddocumentbysupplierid/{supplierid}")
    public ResponseEntity<List<SupplierDocumentEntity>> findDocumentsBySupplierId(@PathVariable("supplierid") Integer supplierId) {
        return new ResponseEntity<List<SupplierDocumentEntity>>(supplierService.findDocumentsBySupplierId(supplierId), HttpStatus.OK);
    }

    @PostMapping("/save/document")
    public ResponseEntity<AdminResponse> saveDocument(@RequestBody SupplierDocumentDto supplierDocumentDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierDocumentEntity entity = supplierService.saveSupplierDocument(supplierDocumentDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify/document")
    public ResponseEntity<AdminResponse> updateDocument(@RequestBody SupplierDocumentDto supplierDocumentDto) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierDocumentEntity entity = supplierService.saveSupplierDocument(supplierDocumentDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/document/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeDocument(@PathVariable("id") Integer id,
                                                        @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            SupplierDocumentEntity entity = supplierService.findSupplierDocumentById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            supplierService.removeSupplierDocument(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Supplier  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<AdminResponse> removeAll(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            supplierService.removeAll(ids);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<AdminResponse> lock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<SupplierEntity> entities = supplierService.findAllEntitiesByIds(ids);
            supplierService.lock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<AdminResponse> unlock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<SupplierEntity> entities = supplierService.findAllEntitiesByIds(ids);
            supplierService.unlock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
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
