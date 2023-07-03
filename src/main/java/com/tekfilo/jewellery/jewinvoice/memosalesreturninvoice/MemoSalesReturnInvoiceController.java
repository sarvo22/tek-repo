package com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesreturninvoice.service.MemoSalesReturnInvoiceService;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import com.tekfilo.jewellery.util.JewResponse;
import com.tekfilo.jewellery.util.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/jew/invoice/memo/sales/return")
public class MemoSalesReturnInvoiceController {

    @Autowired
    MemoSalesReturnInvoiceService memoSalesReturnInvoiceService;


    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<MemoSalesReturnInvoiceMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<MemoSalesReturnInvoiceMainEntity>>(memoSalesReturnInvoiceService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<MemoSalesReturnInvoiceMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoSalesReturnInvoiceMainEntity>(memoSalesReturnInvoiceService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody InvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceMainEntity entity = memoSalesReturnInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Commodity {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<JewResponse> update(@RequestBody InvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceMainEntity entity = memoSalesReturnInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<JewResponse> remove(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceMainEntity entity = memoSalesReturnInvoiceService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);

            List<MemoSalesReturnInvoiceDetailEntity> detailEntities = memoSalesReturnInvoiceService.findAllDetail(id);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<MemoSalesReturnInvoiceChargesEntity> chargesEntities = memoSalesReturnInvoiceService.findAllICharges(id);
            chargesEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });

            memoSalesReturnInvoiceService.removeAll(entity, detailEntities, chargesEntities);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    // Detail of Sales Invoice

    @GetMapping("/search/detail/{id}")
    public ResponseEntity<List<MemoSalesReturnInvoiceDetailEntity>> findAllDetail(
            @PathVariable("id") Integer id) {
        return new ResponseEntity<List<MemoSalesReturnInvoiceDetailEntity>>(memoSalesReturnInvoiceService.findAllDetail(id), HttpStatus.OK);
    }

    @GetMapping("/search/detailbyrefinvid/{refinvid}/{refinvtype}")
    public ResponseEntity<List<MemoSalesReturnInvoiceDetailEntity>> findAllDetailByReferenceInvoiceIdAndType(
            @PathVariable("refinvid") Integer referenceInvoiceId,
            @PathVariable("refinvtype") String referenceInvoiceType) {
        return new ResponseEntity<List<MemoSalesReturnInvoiceDetailEntity>>(memoSalesReturnInvoiceService.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType), HttpStatus.OK);
    }

    @GetMapping("/detail/findbyid/{id}")
    public ResponseEntity<MemoSalesReturnInvoiceDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoSalesReturnInvoiceDetailEntity>(memoSalesReturnInvoiceService.findDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceDetailEntity entity = memoSalesReturnInvoiceService.saveDetail(invoiceDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Commodity {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/detail/saveall")
    public ResponseEntity<JewResponse> saveAllDetail(@RequestBody List<InvoiceDetailDto> invoiceDetailDtoList) {
        JewResponse response = new JewResponse();
        try {
            memoSalesReturnInvoiceService.saveAllDetail(invoiceDetailDtoList);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while save {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/modify")
    public ResponseEntity<JewResponse> updateDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceDetailEntity entity = memoSalesReturnInvoiceService.saveDetail(invoiceDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/remove/{id}")
    public ResponseEntity<JewResponse> removeDetail(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceDetailEntity entity = memoSalesReturnInvoiceService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            memoSalesReturnInvoiceService.removeDetail(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    // Charges of Sales Invoice

    @GetMapping("/search/charge/{id}")
    public ResponseEntity<List<MemoSalesReturnInvoiceChargesEntity>> findAllCharges(
            @PathVariable("id") Integer invId) {
        return new ResponseEntity<List<MemoSalesReturnInvoiceChargesEntity>>(memoSalesReturnInvoiceService.findAllICharges(invId), HttpStatus.OK);
    }


    @GetMapping("/charge/findbyid/{id}")
    public ResponseEntity<MemoSalesReturnInvoiceChargesEntity> findChargesById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoSalesReturnInvoiceChargesEntity>(memoSalesReturnInvoiceService.findChargesById(id), HttpStatus.OK);
    }

    @PostMapping("/charge/save")
    public ResponseEntity<JewResponse> saveCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceChargesEntity entity = memoSalesReturnInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Commodity {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/modify")
    public ResponseEntity<JewResponse> updateCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceChargesEntity entity = memoSalesReturnInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/remove/{id}")
    public ResponseEntity<JewResponse> removeCharges(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceChargesEntity entity = memoSalesReturnInvoiceService.findChargesById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            memoSalesReturnInvoiceService.removeCharges(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/saveall")
    public ResponseEntity<JewResponse> saveAll(@RequestBody InvoiceRequestPayload salesInvoicePayload) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesReturnInvoiceMainEntity entity = memoSalesReturnInvoiceService.createSalesInvoice(salesInvoicePayload);
            response.setId(entity.getId());
            response.setVoucherTypeNo(entity.getInvoiceType().concat("-").concat(entity.getInvoiceNo()));
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception e) {
            response.setId(null);
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            log.error("Exception raised while modifying {}" + e.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/searchproductbykey/{searchkey}")
    public ResponseEntity<List<ProductEntity>> getProductByKey(@PathVariable("searchkey") String searchKey) {
        return new ResponseEntity<List<ProductEntity>>(this.memoSalesReturnInvoiceService.getProductList(searchKey), HttpStatus.OK);
    }

    @PostMapping("/confirm/{memoinvoiceid}")
    public ResponseEntity<JewResponse> confirmSales(@PathVariable("memoinvoiceid") Integer memoInvoiceId) {
        JewResponse response = new JewResponse();
        try {
            String confirmNo = memoSalesReturnInvoiceService.confirmSales(memoInvoiceId);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("confirm_100");
            response.setMessage("Confirm Invoice Generated " + confirmNo);
        } catch (Exception e) {
            response.setId(null);
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            log.error("Exception on Confirm Sales  {}" + e.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/partialconfirm}/{memoinvoiceid}")
    public ResponseEntity<JewResponse> confirmSales(
            @PathVariable("memoinvoiceid") Integer memoInvoiceId,
            @RequestBody List<InvoiceDetailDto> invoiceDetailDtoList) {
        JewResponse response = new JewResponse();
        try {
            String confirmNo = memoSalesReturnInvoiceService.confirmPartialSales(memoInvoiceId, invoiceDetailDtoList);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("confirm_100");
            response.setMessage("Confirm Invoice Generated " + confirmNo);
        } catch (Exception e) {
            response.setId(null);
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            log.error("Exception on Confirm Sales  {}" + e.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/return/{memoinvoiceid}")
    public ResponseEntity<JewResponse> returnMemoSales(@PathVariable("memoinvoiceid") Integer memoInvoiceId,
                                                       @RequestBody List<InvoiceDetailDto> invoiceDetailDtoList) {
        JewResponse response = new JewResponse();
        try {
            memoSalesReturnInvoiceService.partialReturnMemoSales(memoInvoiceId, invoiceDetailDtoList);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("confirm_100");
            response.setMessage("Invoice Returned");
        } catch (Exception e) {
            response.setId(null);
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            log.error("Exception on Return Memo Sales  {}" + e.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/partial/return/{memoinvoiceid}")
    public ResponseEntity<JewResponse> returnMemoSales(@PathVariable("memoinvoiceid") Integer memoInvoiceId) {
        JewResponse response = new JewResponse();
        try {
            memoSalesReturnInvoiceService.returnMemoSales(memoInvoiceId);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("confirm_100");
            response.setMessage("Invoice Returned");
        } catch (Exception e) {
            response.setId(null);
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            log.error("Exception on Return Memo Sales  {}" + e.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/changestatus/{id}")
    public ResponseEntity<JewResponse> changestatus(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            memoSalesReturnInvoiceService.changeStatus(id);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage("Successfully Approved");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<JewResponse> removeAll(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<MemoSalesReturnInvoiceMainEntity> entities = memoSalesReturnInvoiceService.findAllEntitiesByIds(ids);
            List<MemoSalesReturnInvoiceDetailEntity> detailEntities = memoSalesReturnInvoiceService.findAllDetailByMainIds(entities.stream().map(MemoSalesReturnInvoiceMainEntity::getId).collect(Collectors.toList()));
            List<MemoSalesReturnInvoiceChargesEntity> chargesEntities = memoSalesReturnInvoiceService.findAllChargesByMainIds(entities.stream().map(MemoSalesReturnInvoiceMainEntity::getId).collect(Collectors.toList()));
            memoSalesReturnInvoiceService.removeAll(entities.stream().map(MemoSalesReturnInvoiceMainEntity::getId).collect(Collectors.toList()),
                    detailEntities.stream().map(MemoSalesReturnInvoiceDetailEntity::getId).collect(Collectors.toList()),
                    chargesEntities.stream().map(MemoSalesReturnInvoiceChargesEntity::getId).collect(Collectors.toList()));
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


    @PutMapping("/lock")
    public ResponseEntity<JewResponse> lock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<MemoSalesReturnInvoiceMainEntity> entities = memoSalesReturnInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(1);
            });
            memoSalesReturnInvoiceService.lock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<JewResponse> unlock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<MemoSalesReturnInvoiceMainEntity> entities = memoSalesReturnInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(0);
            });
            memoSalesReturnInvoiceService.unlock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }
}
