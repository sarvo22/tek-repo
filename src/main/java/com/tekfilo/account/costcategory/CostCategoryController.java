package com.tekfilo.account.costcategory;

import com.tekfilo.account.base.FilterClause;
import com.tekfilo.account.multitenancy.UserContext;
import com.tekfilo.account.transaction.jv.entity.JVMainEntity;
import com.tekfilo.account.util.AccountResponse;
import com.tekfilo.account.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/account/costcategory")
public class CostCategoryController {

    @Autowired
    CostCategoryService costCategoryService;

    @PostMapping("/main/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<CostCategoryEntity>> findAllMain(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<CostCategoryEntity>>(costCategoryService.findAllMain(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<CostCategoryEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CostCategoryEntity>(costCategoryService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<AccountResponse> save(@RequestBody CostCategoryDto costCategoryDto) {
        AccountResponse response = new AccountResponse();
        try {
            CostCategoryEntity entity = costCategoryService.save(costCategoryDto);
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
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<AccountResponse> update(@RequestBody CostCategoryDto costCategoryDto) {
        AccountResponse response = new AccountResponse();
        try {
            CostCategoryEntity entity = costCategoryService.save(costCategoryDto);
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
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<AccountResponse> remove(@PathVariable("id") Integer id) {
        AccountResponse response = new AccountResponse();
        try {
            CostCategoryEntity entity = costCategoryService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            costCategoryService.remove(entity);
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
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }


}
