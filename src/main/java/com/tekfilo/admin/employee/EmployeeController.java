package com.tekfilo.admin.employee;

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
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<EmployeeEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<EmployeeEntity>>(employeeService.findAll(
                pageNo, pageSize, sortColumn,
                sortdirection == null ? "asc" : sortdirection,
                filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<EmployeeEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<EmployeeEntity>(employeeService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody EmployeeDto employeeDto) {
        AdminResponse response = new AdminResponse();
        try {
            EmployeeEntity entity = employeeService.save(employeeDto);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AdminResponse> update(@RequestBody EmployeeDto employeeDto) {
        AdminResponse response = new AdminResponse();
        try {
            EmployeeEntity entity = employeeService.save(employeeDto);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}/{operateby}")
    public ResponseEntity<AdminResponse> remove(@PathVariable("id") Integer id,
                                                @PathVariable("operateby") Integer operateBy) {
        AdminResponse response = new AdminResponse();
        try {
            EmployeeEntity entity = employeeService.findById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            employeeService.remove(entity);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/removeall")
    public ResponseEntity<AdminResponse> remove(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<EmployeeEntity> entities = employeeService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            employeeService.removeAll(entities);
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
            List<EmployeeEntity> entities = employeeService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            employeeService.lock(entities);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<AdminResponse> unlock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<EmployeeEntity> entities = employeeService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            employeeService.unlock(entities);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/updatepicturepath")
    public ResponseEntity<AdminResponse> updatePicturePath(@RequestBody Map<String, Object> data) {
        AdminResponse response = new AdminResponse();
        try {
            Integer id = Integer.parseInt(data.get("id").toString());
            String picName = (String) data.get("picName");
            EmployeeEntity entity = employeeService.findById(id);
            entity.setPicName(picName);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            employeeService.updatePicturePath(entity);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Uploaded Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }
}
