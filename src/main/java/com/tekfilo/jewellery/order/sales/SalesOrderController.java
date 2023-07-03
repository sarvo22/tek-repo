package com.tekfilo.jewellery.order.sales;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.order.sales.dto.SalesOrderDetailDto;
import com.tekfilo.jewellery.order.sales.dto.SalesOrderMainDto;
import com.tekfilo.jewellery.order.sales.entity.SalesOrderDetailEntity;
import com.tekfilo.jewellery.order.sales.entity.SalesOrderMainEntity;
import com.tekfilo.jewellery.order.sales.service.SalesOrderService;
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
@RequestMapping("/jew/so")
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
    public ResponseEntity<JewResponse> saveOrderMain(@RequestBody SalesOrderMainDto salesOrderMainDto) {
        JewResponse response = new JewResponse();
        try {
            salesOrderService.saveSalesOrderMain(salesOrderMainDto);
            response.setLangStatus(MessageConstants.LANG_SAVE_SUCCESS_STATUS);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setMessage(MessageConstants.RECORD_SAVE);
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
    public ResponseEntity<JewResponse> updateOrderMain(@RequestBody SalesOrderMainDto salesOrderMainDto) {
        JewResponse response = new JewResponse();
        try {
            salesOrderService.modifySalesOrderMain(salesOrderMainDto);
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
    public ResponseEntity<JewResponse> remove(@PathVariable("id") Integer salesOrderId) {
        JewResponse response = new JewResponse();
        try {
            salesOrderService.removeSalesOrder(salesOrderId, UserContext.getLoggedInUser());
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

    @GetMapping("/findalldetailbymain/{salesorderid}")
    public ResponseEntity<List<SalesOrderDetailEntity>> findOrderDetailByMain(@PathVariable("salesorderid") Integer salesOrderId) {
        return new ResponseEntity<List<SalesOrderDetailEntity>>(salesOrderService.findSalesOrderDetailListByMain(salesOrderId), HttpStatus.OK);
    }

    @PostMapping("/savedetail")
    public ResponseEntity<JewResponse> saveOrderDetail(@RequestBody SalesOrderDetailDto salesOrderDetailDto) {
        JewResponse response = new JewResponse();
        try {
            salesOrderService.saveSalesOrderDetail(salesOrderDetailDto);
            response.setLangStatus("save_100");
            response.setStatus(100);
            response.setMessage(MessageConstants.RECORD_SAVE);
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
    public ResponseEntity<JewResponse> modifyOrderDetail(@RequestBody SalesOrderDetailDto salesOrderDetailDto) {
        JewResponse response = new JewResponse();
        try {
            salesOrderService.modifySalesOrderDetail(salesOrderDetailDto);
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

    @PutMapping("/removedetail/{salesorderdetailid}")
    public ResponseEntity<JewResponse> modifyOrderDetail(
            @PathVariable("salesorderdetailid") Integer salesOrderDetailId) {
        JewResponse response = new JewResponse();
        try {
            salesOrderService.removeSalesOrderDetail(salesOrderDetailId, UserContext.getLoggedInUser());
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

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<SalesOrderMainEntity> findById(@PathVariable("id") Integer salesOrderId) {
        return new ResponseEntity<SalesOrderMainEntity>(salesOrderService.findSalesOrderById(salesOrderId), HttpStatus.OK);
    }

    @PutMapping("/lock")
    public ResponseEntity<JewResponse> lock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<SalesOrderMainEntity> entities = salesOrderService.findMainListByIds(ids);
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
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<JewResponse> unlock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<SalesOrderMainEntity> entities = salesOrderService.findMainListByIds(ids);
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
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }
}
