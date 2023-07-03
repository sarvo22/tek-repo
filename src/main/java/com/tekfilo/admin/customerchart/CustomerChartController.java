package com.tekfilo.admin.customerchart;

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
@RequestMapping("/admin/customerchart")
public class CustomerChartController {

    @Autowired
    CustomerChartService customerChartService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<CustomerChartEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<CustomerChartEntity>>(customerChartService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<CustomerChartEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CustomerChartEntity>(customerChartService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/finddetailbyid/{id}")
    public ResponseEntity<CustomerChartDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CustomerChartDetailEntity>(customerChartService.findDetailById(id), HttpStatus.OK);
    }

    @GetMapping("/finddetailbymainid/{id}")
    public ResponseEntity<List<CustomerChartDetailEntity>> findDetailByMainId(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<CustomerChartDetailEntity>>(customerChartService.findDetailByMainId(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AdminResponse> save(@RequestBody CustomerChartDto customerChartDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerChartEntity entity = customerChartService.save(customerChartDto);
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
    public ResponseEntity<AdminResponse> saveDetail(@RequestBody CustomerChartDetailDto customerChartDetailDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerChartDetailEntity entity = customerChartService.saveDetail(customerChartDetailDto);
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
    public ResponseEntity<AdminResponse> update(@RequestBody CustomerChartDto customerChartDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerChartEntity entity = customerChartService.save(customerChartDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<AdminResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifydetail")
    public ResponseEntity<AdminResponse> updateDetail(@RequestBody CustomerChartDetailDto customerChartDetailDto) {
        AdminResponse response = new AdminResponse();
        try {
            CustomerChartDetailEntity entity = customerChartService.saveDetail(customerChartDetailDto);
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
            CustomerChartEntity entity = customerChartService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            customerChartService.remove(entity);
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
            CustomerChartDetailEntity entity = customerChartService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            customerChartService.removeDetail(entity);
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
