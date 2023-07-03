package com.tekfilo.admin.department;

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
@RequestMapping("/admin/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<DepartmentEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<DepartmentEntity>>(departmentService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<DepartmentEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<DepartmentEntity>(departmentService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody DepartmentDto departmentDto) {
        AdminResponse response = new AdminResponse();
        try {
            DepartmentEntity entity = departmentService.save(departmentDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_SUCCESS);
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
    public ResponseEntity<AdminResponse> update(@RequestBody DepartmentDto departmentDto) {
        AdminResponse response = new AdminResponse();
        try {
            DepartmentEntity entity = departmentService.save(departmentDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_UPDATED);
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
            DepartmentEntity entity = departmentService.findById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            departmentService.remove(entity);
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
            List<DepartmentEntity> entities = departmentService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            departmentService.removeAll(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_DELETED);
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity Group {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<AdminResponse> lock(@RequestBody List<Integer> ids) {
        AdminResponse response = new AdminResponse();
        try {
            List<DepartmentEntity> entities = departmentService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            departmentService.lock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus(MessageConstants.LANG_STATUS_LOCKED);
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
            List<DepartmentEntity> entities = departmentService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            departmentService.unlock(entities);
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
