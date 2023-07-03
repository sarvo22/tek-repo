package com.tekfilo.admin.customer;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.customer.dto.*;
import com.tekfilo.admin.customer.entity.*;
import com.tekfilo.admin.customer.service.CustomerService;
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
@RequestMapping("/admin/customer")
public class CustomerController {


    @Autowired
    CustomerService customerService;


    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<CustomerEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<CustomerEntity>>(customerService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbykey/{key}")
    public ResponseEntity<List<CustomerEntity>> findByKey(@PathVariable("key") String key) {
        return new ResponseEntity<List<CustomerEntity>>(this.customerService.getCustomerList(key), HttpStatus.OK);
    }


    @GetMapping("/findbyid/{id}")
    public ResponseEntity<CustomerEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CustomerEntity>(customerService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody CustomerDto customerDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerEntity entity = customerService.save(customerDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AdminResponse> update(@RequestBody CustomerDto customerDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerEntity entity = customerService.save(customerDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}/{operateby}")
    public ResponseEntity<AdminResponse> remove(@PathVariable("id") Integer id,
                                                @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerEntity entity = customerService.findById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            customerService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/search/address/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<CustomerAddressEntity>> findAllCustomerAddressCustomerAddress(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<CustomerAddressEntity>>(customerService.findAllCustomerAddress(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/address/findbyid/{id}")
    public ResponseEntity<CustomerAddressEntity> findCustomerAddressById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CustomerAddressEntity>(customerService.findCustomerAddressById(id), HttpStatus.OK);
    }

    @GetMapping("/address/findbycustomerid/{customerid}")
    public ResponseEntity<List<CustomerAddressEntity>> findAddressByCustomerId(@PathVariable("customerid") Integer customerId) {
        return new ResponseEntity<List<CustomerAddressEntity>>(customerService.findAddressByCustomerId(customerId), HttpStatus.OK);

    }

    @PostMapping("/save/address")
    public ResponseEntity<AdminResponse> saveAddress(@RequestBody CustomerAddressDto customerAddressDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerAddressEntity entity = customerService.saveCustomerAddress(customerAddressDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify/address")
    public ResponseEntity<AdminResponse> updateAddress(@RequestBody CustomerAddressDto customerAddressDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerAddressEntity entity = customerService.saveCustomerAddress(customerAddressDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/address/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeAddress(@PathVariable("id") Integer id,
                                                       @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerAddressEntity entity = customerService.findCustomerAddressById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            customerService.removeCustomerAddress(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/search/contact/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<CustomerContactEntity>> findAllCustomerContact(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<CustomerContactEntity>>(customerService.findAllCustomerContact(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/contact/findbyid/{id}")
    public ResponseEntity<CustomerContactEntity> findCustomerContactById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CustomerContactEntity>(customerService.findCustomerContactById(id), HttpStatus.OK);
    }

    @GetMapping("/contact/findbycustomerid/{customerid}")
    public ResponseEntity<List<CustomerContactEntity>> findContactsByCustomerId(@PathVariable("customerid") Integer customerId) {
        return new ResponseEntity<List<CustomerContactEntity>>(customerService.findContactsByCustomerId(customerId), HttpStatus.OK);
    }

    @PostMapping("/save/contact")
    public ResponseEntity<AdminResponse> saveContact(@RequestBody CustomerContactDto customerContactDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerContactEntity entity = customerService.saveCustomerContact(customerContactDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify/contact")
    public ResponseEntity<AdminResponse> updateContact(@RequestBody CustomerContactDto customerContactDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerContactEntity entity = customerService.saveCustomerContact(customerContactDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/contact/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeContact(@PathVariable("id") Integer id,
                                                       @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerContactEntity entity = customerService.findCustomerContactById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            customerService.removeCustomerContact(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    // Customer Limit

    @GetMapping("/search/limit/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<CustomerLimitEntity>> findAllCustomerLimit(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<CustomerLimitEntity>>(customerService.findAllCustomerLimit(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/limit/findbyid/{id}")
    public ResponseEntity<CustomerLimitEntity> findCustomerLimitById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CustomerLimitEntity>(customerService.findCustomerLimitById(id), HttpStatus.OK);
    }

    @GetMapping("/limit/findbycustomerid/{customerid}")
    public ResponseEntity<List<CustomerLimitEntity>> findLimitsByCustomerId(@PathVariable("customerid") Integer customerId) {
        return new ResponseEntity<List<CustomerLimitEntity>>(customerService.findLimitsByCustomerId(customerId), HttpStatus.OK);

    }

    @PostMapping("/save/limit")
    public ResponseEntity<AdminResponse> saveLimit(@RequestBody CustomerLimitDto customerLimitDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerLimitEntity entity = customerService.saveCustomerLimit(customerLimitDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify/limit")
    public ResponseEntity<AdminResponse> updateLimit(@RequestBody CustomerLimitDto customerLimitDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerLimitEntity entity = customerService.saveCustomerLimit(customerLimitDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/limit/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeLimit(@PathVariable("id") Integer id,
                                                     @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerLimitEntity entity = customerService.findCustomerLimitById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            customerService.removeCustomerLimit(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    // Customer Document

    @GetMapping("/search/document/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<List<CustomerDocumentEntity>> findAllCustomerDocument(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection) {
        return new ResponseEntity<List<CustomerDocumentEntity>>(customerService.findAllCustomerDocument(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection), HttpStatus.OK);
    }


    @GetMapping("/document/findbyid/{id}")
    public ResponseEntity<CustomerDocumentEntity> findCustomerDocumentById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CustomerDocumentEntity>(customerService.findCustomerDocumentById(id), HttpStatus.OK);
    }

    @GetMapping("/document/finddocumentbycustomerid/{customerid}")
    public ResponseEntity<List<CustomerDocumentEntity>> findDocumentsByCustomerId(@PathVariable("customerid") Integer customerId) {
        return new ResponseEntity<List<CustomerDocumentEntity>>(customerService.findDocumentsByCustomerId(customerId), HttpStatus.OK);
    }

    @PostMapping("/save/document")
    public ResponseEntity<AdminResponse> saveDocument(@RequestBody CustomerDocumentDto customerDocumentDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerDocumentEntity entity = customerService.saveCustomerDocument(customerDocumentDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify/document")
    public ResponseEntity<AdminResponse> updateDocument(@RequestBody CustomerDocumentDto customerDocumentDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerDocumentEntity entity = customerService.saveCustomerDocument(customerDocumentDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/document/{id}/{operateby}")
    public ResponseEntity<AdminResponse> removeDocument(@PathVariable("id") Integer id,
                                                        @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerDocumentEntity entity = customerService.findCustomerDocumentById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            customerService.removeCustomerDocument(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<AdminResponse> removeAll(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            Integer userId = UserContext.getLoggedInUser();
            List<CustomerDocumentEntity> documentEntities = customerService.findAllDocumentByCustomerIds(ids);
            documentEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<CustomerContactEntity> contactEntities = customerService.findAllContactByCustomerIds(ids);
            contactEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<CustomerLimitEntity> limitEntities = customerService.findAllLimitByCustomerIds(ids);
            limitEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<CustomerAddressEntity> addressEntities = customerService.findAllAddressCustomerIds(ids);
            addressEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            List<CustomerEntity> customerEntities = customerService.findCustomerByIds(ids);
            customerEntities.stream().forEach(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(userId);
            });
            customerService.removeAll(documentEntities, contactEntities, limitEntities, addressEntities, customerEntities);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/lock")
    public ResponseEntity<AdminResponse> lock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<CustomerEntity> entities = customerService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            customerService.lock(entities);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<AdminResponse> unlock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<CustomerEntity> entities = customerService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            customerService.unlock(entities);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }
}
