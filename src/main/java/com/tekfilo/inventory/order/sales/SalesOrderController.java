package com.tekfilo.inventory.order.sales;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.order.sales.dto.SalesOrderDetailDto;
import com.tekfilo.inventory.order.sales.dto.SalesOrderMainDto;
import com.tekfilo.inventory.order.sales.entity.SalesOrderDetailEntity;
import com.tekfilo.inventory.order.sales.entity.SalesOrderMainEntity;
import com.tekfilo.inventory.order.sales.service.SalesOrderService;
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
@RequestMapping("/inventory/so")
public class SalesOrderController {


    @Autowired
    SalesOrderService salesOrderService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<SalesOrderMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<SalesOrderMainEntity>>(salesOrderService.findAllSalesOrders(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> saveOrderMain(@RequestBody SalesOrderMainDto salesOrderMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            Integer id = salesOrderService.saveSalesOrderMain(salesOrderMainDto);
            response.setLangStatus(MessageConstants.LANG_SAVE_SUCCESS_STATUS);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setMessage("Sales Order Main Created");
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
    public ResponseEntity<InventoryResponse> updateOrderMain(@RequestBody SalesOrderMainDto salesOrderMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            salesOrderService.modifySalesOrderMain(salesOrderMainDto);
            response.setLangStatus(MessageConstants.LANG_UPDATE_SUCCESS_STATUS);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setMessage("Sales Order Main Modified");
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
    public ResponseEntity<InventoryResponse> remove(@PathVariable("id") Integer salesOrderId,
                                                    @PathVariable("operateby") Integer operateBy) {
        InventoryResponse response = new InventoryResponse();
        try {
            salesOrderService.removeSalesOrder(salesOrderId, operateBy);
            response.setLangStatus("remove_100");
            response.setStatus(100);
            response.setMessage("Sales Order Detail and Main Removed");
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("remove_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findalldetailbymain/{salesorderid}")
    public ResponseEntity<List<SalesOrderDetailEntity>> findOrderDetailByMain(@PathVariable("salesorderid") Integer salesOrderId) {
        return new ResponseEntity<List<SalesOrderDetailEntity>>(salesOrderService.findSalesOrderDetailListByMain(salesOrderId), HttpStatus.OK);
    }

    @PostMapping("/savedetail")
    public ResponseEntity<InventoryResponse> saveOrderDetail(@RequestBody SalesOrderDetailDto salesOrderDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            SalesOrderDetailEntity entity = salesOrderService.saveSalesOrderDetail(salesOrderDetailDto);
            response.setLangStatus("save_100");
            response.setStatus(100);
            response.setMessage("Sales Order Detail Created");
            response.setId(entity.getId());
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
    public ResponseEntity<InventoryResponse> modifyOrderDetail(@RequestBody SalesOrderDetailDto salesOrderDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            salesOrderService.modifySalesOrderDetail(salesOrderDetailDto);
            response.setLangStatus("modify_100");
            response.setStatus(100);
            response.setMessage("Sales Order Detail Modified");
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("modify_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removedetail/{salesorderdetailid}/{operateby}")
    public ResponseEntity<InventoryResponse> modifyOrderDetail(
            @PathVariable("salesorderdetailid") Integer salesOrderDetailId,
            @PathVariable("operateby") Integer operateBy) {
        InventoryResponse response = new InventoryResponse();
        try {
            salesOrderService.removeSalesOrderDetail(salesOrderDetailId, operateBy);
            response.setLangStatus("modify_100");
            response.setStatus(100);
            response.setMessage("Sales Order Detail Modified");
        } catch (Exception e) {
            response.setStatus(101);
            response.setLangStatus("modify_101");
            response.setMessage(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            log.error(e.getCause() == null ? e.getMessage() : e.getCause().getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<SalesOrderMainEntity> findById(@PathVariable("id") Integer salesOrderId) {
        return new ResponseEntity<SalesOrderMainEntity>(salesOrderService.findSalesOrderById(salesOrderId), HttpStatus.OK);
    }

    @GetMapping("/finddetailbyid/{id}")
    public ResponseEntity<SalesOrderDetailEntity> findDetailById(@PathVariable("id") Integer salesOrderDetailId) {
        return new ResponseEntity<SalesOrderDetailEntity>(salesOrderService.findSalesOrderDetailById(salesOrderDetailId), HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> remove(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<SalesOrderMainEntity> entities = salesOrderService.findAllEntitiesByIds(ids);
            salesOrderService.removeAll(entities);
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
            List<SalesOrderMainEntity> entities = salesOrderService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            salesOrderService.lock(entities);
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
            List<SalesOrderMainEntity> entities = salesOrderService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            salesOrderService.unlock(entities);
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
