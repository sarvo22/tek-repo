package com.tekfilo.jewellery.production.studinvoice;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.production.common.ProductionInvoiceComponentDto;
import com.tekfilo.jewellery.production.common.ProductionInvoiceDetailDto;
import com.tekfilo.jewellery.production.common.ProductionInvoiceMainDto;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceComponentEntity;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceDetailEntity;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceMainEntity;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceComponentEntity;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceDetailEntity;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceMainEntity;
import com.tekfilo.jewellery.production.studinvoice.service.StudInvoiceService;
import com.tekfilo.jewellery.util.JewResponse;
import com.tekfilo.jewellery.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/jew/stud")
public class StudInvoiceController {


    @Autowired
    StudInvoiceService studInvoiceService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<StudInvoiceMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<StudInvoiceMainEntity>>
                (studInvoiceService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<StudInvoiceMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<StudInvoiceMainEntity>(studInvoiceService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody ProductionInvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            StudInvoiceMainEntity entity = studInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<JewResponse> update(@RequestBody ProductionInvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            StudInvoiceMainEntity entity = studInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<JewResponse> remove(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            StudInvoiceMainEntity entity = studInvoiceService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            studInvoiceService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PostMapping("/savedetail")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody ProductionInvoiceDetailDto productionInvoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            StudInvoiceDetailEntity entity = studInvoiceService.saveDetail(productionInvoiceDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/savedetaillist")
    public ResponseEntity<JewResponse> saveDetailList(@RequestBody List<ProductionInvoiceDetailDto> productionInvoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            studInvoiceService.saveDetailList(productionInvoiceDetailDto);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/finddetailbymainid/{id}")
    public ResponseEntity<List<StudInvoiceDetailEntity>> findDetailByMainId(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<StudInvoiceDetailEntity>>(studInvoiceService.findDetailByMainId(id), HttpStatus.OK);
    }

    @GetMapping("/findcomponentbydetailid/{id}")
    public ResponseEntity<List<StudInvoiceComponentEntity>> findComponentsByDetailId(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<StudInvoiceComponentEntity>>(studInvoiceService.findComponentByDetailId(id), HttpStatus.OK);
    }


    @PostMapping("/savecomponent")
    public ResponseEntity<JewResponse> saveComponent(@RequestBody ProductionInvoiceComponentDto productionInvoiceComponentDto) {
        JewResponse response = new JewResponse();
        try {
            StudInvoiceComponentEntity entity = studInvoiceService.saveComponent(productionInvoiceComponentDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/deletestuddetail")
    public ResponseEntity<JewResponse> removeDetail(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            studInvoiceService.deleteDetail(id);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/deletestudcomponent")
    public ResponseEntity<JewResponse> removeComponent(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            studInvoiceService.deleteComponent(id);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/lock")
    public ResponseEntity<JewResponse> lock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<StudInvoiceMainEntity> entities = studInvoiceService.findMainListByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            studInvoiceService.lock(entities);
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
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<JewResponse> unlock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<StudInvoiceMainEntity> entities = studInvoiceService.findMainListByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            studInvoiceService.unlock(entities);
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
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<JewResponse> removeAll(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<StudInvoiceMainEntity> entities = studInvoiceService.findAllEntitiesByIds(ids);
            List<StudInvoiceDetailEntity> detailEntities = studInvoiceService.findAllDetailByMainIds(entities.stream().map(StudInvoiceMainEntity::getId).collect(Collectors.toList()));
            List<StudInvoiceComponentEntity> componentEntities = studInvoiceService.findAllComponentByMainIds(entities.stream().map(StudInvoiceMainEntity::getId).collect(Collectors.toList()));
            studInvoiceService.removeAll(entities,detailEntities,componentEntities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }
}
