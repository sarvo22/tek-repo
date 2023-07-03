package com.tekfilo.inventory.commodity;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.util.InventoryResponse;
import com.tekfilo.inventory.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/commodity")
@Slf4j
public class CommodityController {

    @Autowired
    CommodityService commodityService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<CommodityEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<CommodityEntity>>(commodityService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection,
                filterClause), HttpStatus.OK);
    }


    @GetMapping("/findbyid/{id}")
    public ResponseEntity<CommodityEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CommodityEntity>(commodityService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> save(@RequestBody CommodityDto commodityDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            int count = this.commodityService.checkNameExists(commodityDto.getName());
            if (count > 0) {
                response.setStatus(MessageConstants.ERROR_STATUS);
                response.setLangStatus("error_101");
                response.setMessage(MessageConstants.COMMODITY_EXITS);
                return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
            }
            CommodityEntity entity = commodityService.save(commodityDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus(MessageConstants.LANG_STATUS_SUCCESS);
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Commodity {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<InventoryResponse> update(@RequestBody CommodityDto commodityDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            CommodityEntity entity = commodityService.save(commodityDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus(MessageConstants.LANG_STATUS_UPDATED);
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}/{operateby}")
    public ResponseEntity<InventoryResponse> remove(@PathVariable("id") Integer id,
                                                    @PathVariable("operateby") Integer operateBy) {
        InventoryResponse response = new InventoryResponse();
        try {
            CommodityEntity entity = commodityService.findById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            commodityService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus(MessageConstants.LANG_STATUS_DELETED);
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity  {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> remove(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<CommodityEntity> entities = commodityService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            commodityService.removeAll(entities);
            response.setId(null);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus(MessageConstants.LANG_STATUS_DELETED);
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity Group {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<InventoryResponse> lock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<CommodityEntity> entities = commodityService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            commodityService.lock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus(MessageConstants.LANG_STATUS_LOCKED);
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity Group {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<InventoryResponse> unlock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<CommodityEntity> entities = commodityService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            commodityService.unlock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus(MessageConstants.LANG_STATUS_UNLOCKED);
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity Group {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

}
