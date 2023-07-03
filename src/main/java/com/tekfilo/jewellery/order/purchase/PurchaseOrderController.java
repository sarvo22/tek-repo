package com.tekfilo.jewellery.order.purchase;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.order.purchase.dto.*;
import com.tekfilo.jewellery.order.purchase.entity.*;
import com.tekfilo.jewellery.order.purchase.service.PurchaseOrderService;
import com.tekfilo.jewellery.util.JewResponse;
import com.tekfilo.jewellery.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/jew/po")
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
    public ResponseEntity<JewResponse> saveOrderMain(@RequestBody PurchaseOrderMainDto purchaseOrderMainDto) {
        JewResponse response = new JewResponse();
        try {
            Integer id = purchaseOrderService.savePurchaseOrderMain(purchaseOrderMainDto);
            response.setLangStatus(MessageConstants.LANG_SAVE_SUCCESS_STATUS);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setMessage(MessageConstants.RECORD_SAVE);
            response.setId(id);
        } catch (Exception e) {
            response.setStatus(MessageConstants.ERROR_STATUS);
            response.setLangStatus(MessageConstants.LANG_SAVE_ERROR_STATUS);
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/modify")
    public ResponseEntity<JewResponse> updateOrderMain(@RequestBody PurchaseOrderMainDto purchaseOrderMainDto) {
        JewResponse response = new JewResponse();
        try {
            purchaseOrderService.modifyPurchaseOrderMain(purchaseOrderMainDto);
            response.setLangStatus(MessageConstants.LANG_UPDATE_SUCCESS_STATUS);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("modify_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<JewResponse> remove(@PathVariable("id") Integer purchaseOrderId) {
        JewResponse response = new JewResponse();
        try {
            purchaseOrderService.removePurchaseOrder(purchaseOrderId, UserContext.getLoggedInUser());
            response.setLangStatus("remove_100");
            response.setStatus(100);
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("remove_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<JewResponse> removeAll(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            purchaseOrderService.deleteAll(ids);
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

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<PurchaseOrderMainEntity> findById(@PathVariable("id") Integer purchaseOrderId) {
        return new ResponseEntity<PurchaseOrderMainEntity>(purchaseOrderService.findPurchaseOrderById(purchaseOrderId), HttpStatus.OK);
    }

    @GetMapping("/findalldetailbymain/{purchaseorderid}")
    public ResponseEntity<List<PurchaseOrderDetailEntity>> findOrderDetailByMain(@PathVariable("purchaseorderid") Integer purchaseOrderId) {
        return new ResponseEntity<List<PurchaseOrderDetailEntity>>(purchaseOrderService.findPurchaseOrderDetailListByMain(purchaseOrderId), HttpStatus.OK);
    }

    @PostMapping("/savedetail")
    public ResponseEntity<JewResponse> saveOrderDetail(@RequestBody PurchaseOrderDetailDto purchaseOrderDetailDto) {
        JewResponse response = new JewResponse();
        try {
            Integer id = purchaseOrderService.savePurchaseOrderDetail(purchaseOrderDetailDto);
            response.setLangStatus("save_100");
            response.setStatus(100);
            response.setMessage(MessageConstants.RECORD_SAVE);
            response.setId(id);
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("save_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifydetail")
    public ResponseEntity<JewResponse> modifyOrderDetail(@RequestBody PurchaseOrderDetailDto purchaseOrderDetailDto) {
        JewResponse response = new JewResponse();
        try {
            purchaseOrderService.modifyPurchaseOrderDetail(purchaseOrderDetailDto);
            response.setLangStatus("modify_100");
            response.setStatus(100);
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("modify_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removedetail/{purchaseorderdetailid}")
    public ResponseEntity<JewResponse> modifyOrderDetail(
            @PathVariable("purchaseorderdetailid") Integer purchaseOrderDetailId) {
        JewResponse response = new JewResponse();
        try {
            purchaseOrderService.removePurchaseOrderDetail(purchaseOrderDetailId, UserContext.getLoggedInUser());
            response.setLangStatus("modify_100");
            response.setStatus(100);
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("modify_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/lock")
    public ResponseEntity<JewResponse> lock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<PurchaseOrderMainEntity> entities = purchaseOrderService.findMainListByIds(ids);
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
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<JewResponse> unlock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<PurchaseOrderMainEntity> entities = purchaseOrderService.findMainListByIds(ids);
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
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findlabourbymainid/{id}")
    public ResponseEntity<List<PurchaseOrderDetailLabourEntity>> findLabourById(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<PurchaseOrderDetailLabourEntity>>(this.purchaseOrderService.findPurchaseOrderDetailLabourList(id), HttpStatus.OK);
    }


    @GetMapping("/findcomponentbydetailid/{id}")
    public ResponseEntity<List<PurchaseOrderComponentEntity>> findcomponentbydetailid(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<PurchaseOrderComponentEntity>>(this.purchaseOrderService.findPurchaseOrderComponentByDetailId(id), HttpStatus.OK);
    }

    @PostMapping("/component/save")
    public ResponseEntity<JewResponse> saveComponent(@RequestBody PurchaseOrderComponentDto purchaseOrderComponentDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderComponentEntity entity = purchaseOrderService.savePurchaseOrderComponent(purchaseOrderComponentDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/component/modify")
    public ResponseEntity<JewResponse> updateComponent(@RequestBody PurchaseOrderComponentDto purchaseOrderComponentDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderComponentEntity entity = purchaseOrderService.savePurchaseOrderComponent(purchaseOrderComponentDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/savelabour")
    public ResponseEntity<JewResponse> saveLabour(@RequestBody PurchaseOrderDetailLabourDto purchaseOrderDetailLabourDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailLabourEntity entity = purchaseOrderService.savePurchaseOrderDetailLabour(purchaseOrderDetailLabourDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifylabour")
    public ResponseEntity<JewResponse> updatelabour(@RequestBody PurchaseOrderDetailLabourDto purchaseOrderDetailLabourDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailLabourEntity entity = purchaseOrderService.savePurchaseOrderDetailLabour(purchaseOrderDetailLabourDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removelabour/{id}")
    public ResponseEntity<JewResponse> removeLabour(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailLabourEntity entity = purchaseOrderService.findPurchaseOrderDetailLabourById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            purchaseOrderService.deletePurchaseOrderDetailLabour(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findmouldpartbymainid/{id}")
    public ResponseEntity<List<PurchaseOrderDetailMouldPartEntity>> findPurchaseOrderDetailMouldPartById(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<PurchaseOrderDetailMouldPartEntity>>(this.purchaseOrderService.findPurchaseOrderDetailMouldPartList(id), HttpStatus.OK);
    }

    @PostMapping("/savemouldpart")
    public ResponseEntity<JewResponse> saveMouldPart(@RequestBody PurchaseOrderDetailMouldPartDto purchaseOrderDetailMouldPartDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailMouldPartEntity entity = purchaseOrderService.savePurchaseOrderDetailMouldPart(purchaseOrderDetailMouldPartDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifymouldpart")
    public ResponseEntity<JewResponse> updatemouldpart(@RequestBody PurchaseOrderDetailMouldPartDto purchaseOrderDetailMouldPartDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailMouldPartEntity entity = purchaseOrderService.savePurchaseOrderDetailMouldPart(purchaseOrderDetailMouldPartDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removemouldpart/{id}")
    public ResponseEntity<JewResponse> removeMouldPart(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailMouldPartEntity entity = purchaseOrderService.findPurchaseOrderDetailMouldPartById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            purchaseOrderService.deletePurchaseOrderDetailMouldPart(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findfindingbymainid/{id}")
    public ResponseEntity<List<PurchaseOrderDetailFindingEntity>> findPurchaseOrderDetailFindingById(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<PurchaseOrderDetailFindingEntity>>(this.purchaseOrderService.findPurchaseOrderDetailFindingList(id), HttpStatus.OK);
    }

    @PostMapping("/savefinding")
    public ResponseEntity<JewResponse> saveFinding(@RequestBody PurchaseOrderDetailFindingDto purchaseOrderDetailFindingDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailFindingEntity entity = purchaseOrderService.savePurchaseOrderDetailFinding(purchaseOrderDetailFindingDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifyfinding")
    public ResponseEntity<JewResponse> updatefinding(@RequestBody PurchaseOrderDetailFindingDto purchaseOrderDetailFindingDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailFindingEntity entity = purchaseOrderService.savePurchaseOrderDetailFinding(purchaseOrderDetailFindingDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removefinding/{id}")
    public ResponseEntity<JewResponse> removeFinding(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailFindingEntity entity = purchaseOrderService.findPurchaseOrderDetailFindingById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            purchaseOrderService.deletePurchaseOrderDetailFinding(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findwaxbymainid/{id}")
    public ResponseEntity<List<PurchaseOrderDetailWaxEntity>> findPurchaseOrderDetailWaxById(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<PurchaseOrderDetailWaxEntity>>(this.purchaseOrderService.findPurchaseOrderDetailWaxList(id), HttpStatus.OK);
    }

    @PostMapping("/savewax")
    public ResponseEntity<JewResponse> saveWax(@RequestBody PurchaseOrderDetailWaxDto purchaseOrderDetailWaxDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailWaxEntity entity = purchaseOrderService.savePurchaseOrderDetailWax(purchaseOrderDetailWaxDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifywax")
    public ResponseEntity<JewResponse> updatewax(@RequestBody PurchaseOrderDetailWaxDto purchaseOrderDetailWaxDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailWaxEntity entity = purchaseOrderService.savePurchaseOrderDetailWax(purchaseOrderDetailWaxDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removewax/{id}")
    public ResponseEntity<JewResponse> removeWax(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            PurchaseOrderDetailWaxEntity entity = purchaseOrderService.findPurchaseOrderDetailWaxById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            purchaseOrderService.deletePurchaseOrderDetailWax(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

}
