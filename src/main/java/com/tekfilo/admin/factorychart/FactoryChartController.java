package com.tekfilo.admin.factorychart;

import com.tekfilo.admin.base.FilterClause;
import com.tekfilo.admin.multitenancy.UserContext;
import com.tekfilo.admin.util.AdminResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/factorychart")
public class FactoryChartController {

    @Autowired
    FactoryChartService factoryChartService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<FactoryChartEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<FactoryChartEntity>>(factoryChartService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<FactoryChartEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<FactoryChartEntity>(factoryChartService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/finddetailbyid/{id}")
    public ResponseEntity<FactoryChartDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<FactoryChartDetailEntity>(factoryChartService.findDetailById(id), HttpStatus.OK);
    }

    @GetMapping("/finddetailbymainid/{id}")
    public ResponseEntity<List<FactoryChartDetailEntity>> findDetailByMainId(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<FactoryChartDetailEntity>>(factoryChartService.findDetailByMainId(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody FactoryChartDto factoryChartDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryChartEntity entity = factoryChartService.save(factoryChartDto);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/savedetail")
    public ResponseEntity<AdminResponse> saveDetail(@RequestBody FactoryChartDetailDto factoryChartDetailDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryChartDetailEntity entity = factoryChartService.saveDetail(factoryChartDetailDto);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AdminResponse> update(@RequestBody FactoryChartDto factoryChartDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryChartEntity entity = factoryChartService.save(factoryChartDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Factory {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifydetail")
    public ResponseEntity<AdminResponse> updateDetail(@RequestBody FactoryChartDetailDto factoryChartDetailDto) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryChartDetailEntity entity = factoryChartService.saveDetail(factoryChartDetailDto);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<AdminResponse> remove(@PathVariable("id") Integer id) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryChartEntity entity = factoryChartService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            factoryChartService.remove(entity);
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
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removedetail/{id}")
    public ResponseEntity<AdminResponse> removeDetail(@PathVariable("id") Integer id) {
        AdminResponse response = new AdminResponse();
        try {
            FactoryChartDetailEntity entity = factoryChartService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            factoryChartService.removeDetail(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }
}
