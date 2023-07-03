package com.tekfilo.inventory.settlement.paymentreceived;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PaymentMainDto;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PendingInvoiceDto;
import com.tekfilo.inventory.settlement.paymentreceived.entity.PaymentReceivedDetailEntity;
import com.tekfilo.inventory.settlement.paymentreceived.entity.PaymentReceivedMainEntity;
import com.tekfilo.inventory.settlement.paymentreceived.service.PaymentReceivedService;
import com.tekfilo.inventory.util.InventoryResponse;
import com.tekfilo.inventory.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/inventory/settlement/paymentreceived")
public class PaymentReceivedController {

    @Autowired
    PaymentReceivedService paymentReceivedService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<PaymentReceivedMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<PaymentReceivedMainEntity>>(
                this.paymentReceivedService.findAll(
                        pageNo, pageSize, sortColumn, sortdirection,
                        filterClauses
                ), HttpStatus.OK
        );
    }


    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> save(@RequestBody PaymentReceivedRequestPayload paymentReceivedRequestPayload) {
        InventoryResponse response = new InventoryResponse();
        try {
            Integer id = this.paymentReceivedService.saveAll(paymentReceivedRequestPayload);
            response.setId(id);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Payment Received Successfully");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            response.setStatus(101);
            response.setLangStatus("save_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/savemain")
    public ResponseEntity<InventoryResponse> saveMain(@RequestBody PaymentMainDto paymentMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            Integer id = this.paymentReceivedService.saveMain(paymentMainDto);
            response.setId(id);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Payment Received Successfully");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            response.setStatus(101);
            response.setLangStatus("save_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<PaymentReceivedMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<PaymentReceivedMainEntity>(this.paymentReceivedService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/finddetailbymainid/{id}")
    public ResponseEntity<List<PaymentReceivedDetailEntity>> findDetailByMainId(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<PaymentReceivedDetailEntity>>(this.paymentReceivedService.findDetailByMainId(id), HttpStatus.OK);
    }

    @GetMapping("/detailbyrefinvid/{refinvid}/{refinvtype}")
    public ResponseEntity<List<PaymentReceivedDetailEntity>> findAllDetailByReferenceInvoiceIdAndType(
            @PathVariable("refinvid") Integer referenceInvoiceId,
            @PathVariable("refinvtype") String referenceInvoiceType) {
        return new ResponseEntity<List<PaymentReceivedDetailEntity>>(paymentReceivedService.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<InventoryResponse> delete(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.paymentReceivedService.deleteMain(id);
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Payment Removed Successfully");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            response.setStatus(101);
            response.setLangStatus("remove_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/delete/{id}")
    public ResponseEntity<InventoryResponse> detailDelete(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            PaymentReceivedDetailEntity entity = this.paymentReceivedService.findDetailById(id);
            entity.setIsDeleted(1);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            this.paymentReceivedService.deleteDetail(entity);
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Payment Removed Successfully");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            response.setStatus(101);
            response.setLangStatus("remove_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/detailsave")
    public ResponseEntity<InventoryResponse> detailSave(@RequestBody List<PendingInvoiceDto> detailList) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.paymentReceivedService.saveDetailList(detailList);
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Added Successfully");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            response.setStatus(101);
            response.setLangStatus("remove_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detailupdate")
    public ResponseEntity<InventoryResponse> detailUpdate(@RequestBody PendingInvoiceDto pendingInvoiceDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.paymentReceivedService.saveDetailList(Arrays.asList(pendingInvoiceDto));
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Detailed Modified Successfully");
        } catch (Exception e) {
            log.error(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            response.setStatus(101);
            response.setLangStatus("remove_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> remove(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<PaymentReceivedMainEntity> entityList = paymentReceivedService.findAllMainByMainIds(ids);
            entityList.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });

            List<PaymentReceivedDetailEntity> detailEntities = paymentReceivedService.findAllDetailByMainIds(ids);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });
            paymentReceivedService.removeAll(entityList, detailEntities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<InventoryResponse> lock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<PaymentReceivedMainEntity> entities = paymentReceivedService.findAllMainByMainIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            paymentReceivedService.lock(entities);
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
            List<PaymentReceivedMainEntity> entities = paymentReceivedService.findAllMainByMainIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            paymentReceivedService.unlock(entities);
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
