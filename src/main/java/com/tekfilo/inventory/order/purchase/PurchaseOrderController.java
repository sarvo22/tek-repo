package com.tekfilo.inventory.order.purchase;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.order.purchase.dto.PurchaseOrderDetailDto;
import com.tekfilo.inventory.order.purchase.dto.PurchaseOrderMainDto;
import com.tekfilo.inventory.order.purchase.entity.PurchaseOrderDetailEntity;
import com.tekfilo.inventory.order.purchase.entity.PurchaseOrderMainEntity;
import com.tekfilo.inventory.order.purchase.service.PurchaseOrderService;
import com.tekfilo.inventory.util.InventoryResponse;
import com.tekfilo.inventory.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inventory/po")
public class PurchaseOrderController {


    @Autowired
    PurchaseOrderService purchaseOrderService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<PurchaseOrderMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<PurchaseOrderMainEntity>>(purchaseOrderService.findAllPurchaseOrders(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> saveOrderMain(@RequestBody PurchaseOrderMainDto purchaseOrderMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            Integer id = purchaseOrderService.savePurchaseOrderMain(purchaseOrderMainDto);
            response.setLangStatus(MessageConstants.LANG_STATUS_SUCCESS);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setMessage(MessageConstants.RECORD_SAVE);
            response.setId(id);
        } catch (Exception e) {
            response.setStatus(MessageConstants.ERROR_STATUS);
            response.setLangStatus(MessageConstants.LANG_SAVE_ERROR_STATUS);
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify")
    public ResponseEntity<InventoryResponse> updateOrderMain(@RequestBody PurchaseOrderMainDto purchaseOrderMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            purchaseOrderService.modifyPurchaseOrderMain(purchaseOrderMainDto);
            response.setLangStatus(MessageConstants.LANG_STATUS_UPDATED);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("modify_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}/{operateby}")
    public ResponseEntity<InventoryResponse> remove(@PathVariable("id") Integer purchaseOrderId,
                                                    @PathVariable("operateby") Integer operateBy) {
        InventoryResponse response = new InventoryResponse();
        try {
            purchaseOrderService.removePurchaseOrder(purchaseOrderId, operateBy);
            response.setLangStatus("remove_100");
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("remove_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<PurchaseOrderMainEntity> findById(@PathVariable("id") Integer purchaseOrderId) {
        return new ResponseEntity<PurchaseOrderMainEntity>(purchaseOrderService.findPurchaseOrderById(purchaseOrderId), HttpStatus.OK);
    }

    @GetMapping("/findalldetailbymain/{purchaseorderid}")
    public ResponseEntity<List<PurchaseOrderDetailEntity>> findOrderDetailByMain(@PathVariable("purchaseorderid") Integer purchaseOrderId) {
        return new ResponseEntity<List<PurchaseOrderDetailEntity>>(purchaseOrderService.findPurchaseOrderDetailListByMain(purchaseOrderId), HttpStatus.OK);
    }

    @PostMapping("/savedetail")
    public ResponseEntity<InventoryResponse> saveOrderDetail(@RequestBody PurchaseOrderDetailDto purchaseOrderDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            Integer id = purchaseOrderService.savePurchaseOrderDetail(purchaseOrderDetailDto);
            response.setLangStatus("save_100");
            response.setStatus(100);
            response.setMessage("Purchase Order Detail Created");
            response.setId(id);
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("save_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifydetail")
    public ResponseEntity<InventoryResponse> modifyOrderDetail(@RequestBody PurchaseOrderDetailDto purchaseOrderDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            purchaseOrderService.modifyPurchaseOrderDetail(purchaseOrderDetailDto);
            response.setLangStatus("modify_100");
            response.setStatus(100);
            response.setMessage("Purchase Order Detail Modified");
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("modify_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removedetail/{purchaseorderdetailid}/{operateby}")
    public ResponseEntity<InventoryResponse> modifyOrderDetail(
            @PathVariable("purchaseorderdetailid") Integer purchaseOrderDetailId,
            @PathVariable("operateby") Integer operateBy) {
        InventoryResponse response = new InventoryResponse();
        try {
            purchaseOrderService.removePurchaseOrderDetail(purchaseOrderDetailId, operateBy);
            response.setLangStatus("modify_100");
            response.setStatus(100);
            response.setMessage("Purchase Order Detail Modified");
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("modify_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/finddetailbyid/{id}")
    public ResponseEntity<PurchaseOrderDetailEntity> findDetailById(@PathVariable("id") Integer purchaseOrderDetailId) {
        return new ResponseEntity<PurchaseOrderDetailEntity>(purchaseOrderService.findPurchaseOrderDetailById(purchaseOrderDetailId), HttpStatus.OK);
    }


    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> remove(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<PurchaseOrderMainEntity> entities = purchaseOrderService.findAllEntitiesByIds(ids);
            purchaseOrderService.removeAll(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<InventoryResponse> lock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<PurchaseOrderMainEntity> entities = purchaseOrderService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            purchaseOrderService.lock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<InventoryResponse> unlock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<PurchaseOrderMainEntity> entities = purchaseOrderService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            purchaseOrderService.unlock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }
}
