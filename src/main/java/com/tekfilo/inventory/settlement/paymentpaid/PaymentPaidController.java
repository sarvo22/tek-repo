package com.tekfilo.inventory.settlement.paymentpaid;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.settlement.paymentpaid.dto.PaymentPaidMainDto;
import com.tekfilo.inventory.settlement.paymentpaid.entity.PaymentPaidDetailEntity;
import com.tekfilo.inventory.settlement.paymentpaid.entity.PaymentPaidMainEntity;
import com.tekfilo.inventory.settlement.paymentpaid.service.PaymentPaidService;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PendingInvoiceDto;
import com.tekfilo.inventory.util.InventoryResponse;
import com.tekfilo.inventory.util.MessageConstants;
import com.tekfilo.inventory.util.TekfiloUtils;
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
@RequestMapping("/inventory/settlement/paymentpaid")
public class PaymentPaidController {

    @Autowired
    PaymentPaidService paymentPaidService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<PaymentPaidMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<PaymentPaidMainEntity>>(
                this.paymentPaidService.findAll(
                        pageNo, pageSize, sortColumn, sortdirection,
                        filterClauses
                ), HttpStatus.OK
        );
    }

    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> save(@RequestBody PaymentPaidRequestPayload paymentPaidRequestPayload) {
        InventoryResponse response = new InventoryResponse();
        try {
            Integer id = this.paymentPaidService.saveAll(paymentPaidRequestPayload);
            response.setId(id);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Payment Paid Successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            TekfiloUtils.setErrorResponse(response,e);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/savemain")
    public ResponseEntity<InventoryResponse> saveMain(@RequestBody PaymentPaidMainDto paymentPaidMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            Integer id = this.paymentPaidService.saveMain(paymentPaidMainDto);
            response.setId(id);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Payment Paid Successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            TekfiloUtils.setErrorResponse(response,e);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<PaymentPaidMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<PaymentPaidMainEntity>(this.paymentPaidService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/detailbyrefinvid/{refinvid}/{refinvtype}")
    public ResponseEntity<List<PaymentPaidDetailEntity>> findAllDetailByReferenceInvoiceIdAndType(
            @PathVariable("refinvid") Integer referenceInvoiceId,
            @PathVariable("refinvtype") String referenceInvoiceType) {
        return new ResponseEntity<List<PaymentPaidDetailEntity>>(paymentPaidService.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType), HttpStatus.OK);
    }

    @GetMapping("/finddetailbymainid/{id}")
    public ResponseEntity<List<PaymentPaidDetailEntity>> findDetailByMainId(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<PaymentPaidDetailEntity>>(this.paymentPaidService.findDetailByMainId(id), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<InventoryResponse> delete(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.paymentPaidService.deleteMain(id);
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Payment Removed Successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            TekfiloUtils.setErrorResponse(response,e);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/delete/{id}")
    public ResponseEntity<InventoryResponse> detailDelete(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            PaymentPaidDetailEntity entity = this.paymentPaidService.findDetailById(id);
            entity.setIsDeleted(1);
            this.paymentPaidService.deleteDetail(entity);
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Payment Removed Successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            TekfiloUtils.setErrorResponse(response,e);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/detailsave")
    public ResponseEntity<InventoryResponse> detailSave(@RequestBody List<PendingInvoiceDto> detailList) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.paymentPaidService.saveDetailList(detailList);
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Added Successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            TekfiloUtils.setErrorResponse(response,e);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detailupdate")
    public ResponseEntity<InventoryResponse> detailUpdate(@RequestBody PendingInvoiceDto pendingInvoiceDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.paymentPaidService.saveDetailList(Arrays.asList(pendingInvoiceDto));
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Detailed Modified Successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            TekfiloUtils.setErrorResponse(response,e);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/findpaymentdetailsbysupplier/{supplierid}")
    public ResponseEntity<List<PaymentPaidDetailEntity>> findPaymentDetailsBySupplier(@PathVariable("supplierid") Integer supplierId) {
        return new ResponseEntity<List<PaymentPaidDetailEntity>>(this.paymentPaidService.findAllDetailBySupplierId(supplierId), HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> remove(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<PaymentPaidMainEntity> entityList = paymentPaidService.findAllMainByMainIds(ids);
            entityList.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });

            List<PaymentPaidDetailEntity> detailEntities = paymentPaidService.findAllDetailByMainIds(ids);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });
            paymentPaidService.removeAll(entityList, detailEntities);
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


    @PutMapping("/lock")
    public ResponseEntity<InventoryResponse> lock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<PaymentPaidMainEntity> entities = paymentPaidService.findAllMainByMainIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            paymentPaidService.lock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<InventoryResponse> unlock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<PaymentPaidMainEntity> entities = paymentPaidService.findAllMainByMainIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            paymentPaidService.unlock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }
}
