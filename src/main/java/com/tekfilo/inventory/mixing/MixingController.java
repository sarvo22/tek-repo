package com.tekfilo.inventory.mixing;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.commodity.CommodityEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceMainEntity;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.util.InventoryResponse;
import com.tekfilo.inventory.util.MessageConstants;
import com.tekfilo.inventory.util.TekfiloUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inventory/mixing")
public class MixingController {

    @Autowired
    MixingService mixingService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<MixingEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<MixingEntity>>(mixingService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> save(@RequestBody MixingCommonDto input) {
        InventoryResponse response = new InventoryResponse();
        try {
            mixingService.save(input);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception e) {
            response.setLangStatus("save_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/findbyid/{id}")
    public ResponseEntity<MixingEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MixingEntity>(this.mixingService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/findalldetail/{mainid}")
    public ResponseEntity<List<MixingDetailEntity>> findDetailByMainId(@PathVariable("mainid") Integer mainId) {
        return new ResponseEntity<List<MixingDetailEntity>>(mixingService.findAllDetailByMainId(mainId), HttpStatus.OK);
    }

    @GetMapping("/findalldetailwithstock/{mainid}")
    public ResponseEntity<List<MixingDetailEntity>> findDetailWithStockByMainId(@PathVariable("mainid") Integer mainId) {
        return new ResponseEntity<List<MixingDetailEntity>>(mixingService.findAllDetailWithStockByMainId(mainId), HttpStatus.OK);
    }

    @PostMapping("/savemain")
    public ResponseEntity<InventoryResponse> saveMainMixing(@RequestBody MixingDto mixingDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            MixingEntity entity = this.mixingService.saveMain(mixingDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception e) {
            response.setLangStatus("save_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifymain")
    public ResponseEntity<InventoryResponse> updateMain(@RequestBody MixingDto mixingDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.mixingService.saveMain(mixingDto);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus("remove_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception e) {
            response.setLangStatus("modify_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/delete/{id}")
    public ResponseEntity<InventoryResponse> delete(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.mixingService.deleteAll(id);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("remove_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception e) {
            response.setLangStatus("remove_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PostMapping("/savedetail")
    public ResponseEntity<InventoryResponse> saveDetail(@RequestBody MixingDetailDto mixingDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            MixingDetailEntity entity = this.mixingService.saveDetail(mixingDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception e) {
            response.setLangStatus("save_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/updatedetail")
    public ResponseEntity<InventoryResponse> updateDetail(@RequestBody MixingDetailDto mixingDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            MixingDetailEntity entity = this.mixingService.saveDetail(mixingDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception e) {
            response.setLangStatus("modify_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/deletedetail/{id}")
    public ResponseEntity<InventoryResponse> deleteDetail(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            MixingDetailEntity entity = mixingService.findDetailById(id);
            entity.setIsDeleted(1);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            mixingService.deleteDetail(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception e) {
            response.setLangStatus("modify_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/searchproductbykey/{searchkey}")
    public ResponseEntity<List<ProductEntity>> getProductByKey(@PathVariable("searchkey") String searchKey) {
        return new ResponseEntity<List<ProductEntity>>(mixingService.getProductList(searchKey), HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<InventoryResponse> lock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<MixingEntity> entities = mixingService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            mixingService.lock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus(MessageConstants.LANG_STATUS_LOCKED);
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<InventoryResponse> unlock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<MixingEntity> entities = mixingService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            mixingService.unlock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus(MessageConstants.LANG_STATUS_UNLOCKED);
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> remove(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<MixingEntity> entityList = mixingService.findAllEntitiesByIds(ids);
            entityList.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });

            List<MixingDetailEntity> detailEntities = mixingService.findAllDetailByMainIds(ids);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });
            mixingService.removeAll(entityList, detailEntities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }
}
