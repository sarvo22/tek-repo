package com.tekfilo.sso.parameter;

import com.tekfilo.sso.base.FilterClause;
import com.tekfilo.sso.base.SSOResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sso/parameter")
public class ParameterController {

    @Autowired
    ParameterService parameterService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<ParameterEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<ParameterEntity>>(parameterService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<ParameterEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<ParameterEntity>(parameterService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<SSOResponse> save(@RequestBody ParameterDto parameterDto) {
        SSOResponse response = new SSOResponse();
        try {
            ParameterEntity entity = parameterService.save(parameterDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<SSOResponse> update(@RequestBody ParameterDto parameterDto) {
        SSOResponse response = new SSOResponse();
        try {
            ParameterEntity entity = parameterService.save(parameterDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<SSOResponse> remove(@PathVariable("id") Integer id) {
        SSOResponse response = new SSOResponse();
        try {
            ParameterEntity entity = parameterService.findById(id);
            entity.setIsDeleted(1);
            parameterService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/removeall")
    public ResponseEntity<SSOResponse> remove(@RequestBody List<Integer> ids) {
        SSOResponse response = new SSOResponse();
        try {
//            List<ParameterEntity> entities = parameterService.findAllEntitiesByIds(ids);
//            entities.stream().forEach(entity -> {
//                entity.setModifiedBy(UserContext.getLoggedInUser());
//                entity.setIsDeleted(1);
//            });
//            parameterService.removeAll(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity Group {}" + exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<SSOResponse> lock(@RequestBody List<Integer> ids) {
        SSOResponse response = new SSOResponse();
        try {
//            List<ParameterEntity> entities = parameterService.findAllEntitiesByIds(ids);
//            entities.stream().forEach(entity -> {
//                entity.setModifiedBy(UserContext.getLoggedInUser());
//                entity.setIsLocked(1);
//            });
//            parameterService.lock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage("Locked Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<SSOResponse> unlock(@RequestBody List<Integer> ids) {
        SSOResponse response = new SSOResponse();
        try {
//            List<ParameterEntity> entities = parameterService.findAllEntitiesByIds(ids);
//            entities.stream().forEach(entity -> {
//                entity.setModifiedBy(UserContext.getLoggedInUser());
//                entity.setIsLocked(0);
//            });
//            parameterService.unlock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage("UnLocked Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity Group {}" + exception.getMessage());
        }
        return new ResponseEntity<SSOResponse>(response, HttpStatus.OK);
    }
}
