package com.tekfilo.jewellery.jewinvoice.memosalesinvoice;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.entity.MemoSalesInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.memosalesinvoice.service.MemoSalesInvoiceService;
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
@RequestMapping("/jew/invoice/memo/sales")
public class MemoSalesInvoiceController {


    @Autowired
    MemoSalesInvoiceService memoSalesInvoiceService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<MemoSalesInvoiceMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<MemoSalesInvoiceMainEntity>>(memoSalesInvoiceService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<MemoSalesInvoiceMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoSalesInvoiceMainEntity>(memoSalesInvoiceService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody InvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesInvoiceMainEntity entity = memoSalesInvoiceService.save(invoiceMainDto);
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
            MemoSalesInvoiceMainEntity entity = memoSalesInvoiceService.save(invoiceMainDto);
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
            MemoSalesInvoiceMainEntity entity = memoSalesInvoiceService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);

            List<MemoSalesInvoiceDetailEntity> detailEntities = memoSalesInvoiceService.findAllDetail(id);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<MemoSalesInvoiceChargesEntity> chargesEntities = memoSalesInvoiceService.findAllICharges(id);
            chargesEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });

            memoSalesInvoiceService.removeAll(entity, detailEntities, chargesEntities);

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
    public ResponseEntity<List<MemoSalesInvoiceDetailEntity>> findAllDetail(
            @PathVariable("id") Integer id) {
        return new ResponseEntity<List<MemoSalesInvoiceDetailEntity>>(memoSalesInvoiceService.findAllDetail(id), HttpStatus.OK);
    }


    @GetMapping("/detail/findbyid/{id}")
    public ResponseEntity<MemoSalesInvoiceDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoSalesInvoiceDetailEntity>(memoSalesInvoiceService.findDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesInvoiceDetailEntity entity = memoSalesInvoiceService.saveDetail(invoiceDetailDto);
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

    @PutMapping("/detail/modify")
    public ResponseEntity<JewResponse> updateDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesInvoiceDetailEntity entity = memoSalesInvoiceService.saveDetail(invoiceDetailDto);
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
            MemoSalesInvoiceDetailEntity entity = memoSalesInvoiceService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            memoSalesInvoiceService.removeDetail(entity);
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
    public ResponseEntity<List<MemoSalesInvoiceChargesEntity>> findAllCharges(
            @PathVariable("id") Integer invId) {
        return new ResponseEntity<List<MemoSalesInvoiceChargesEntity>>(memoSalesInvoiceService.findAllICharges(invId), HttpStatus.OK);
    }


    @GetMapping("/charge/findbyid/{id}")
    public ResponseEntity<MemoSalesInvoiceChargesEntity> findChargesById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoSalesInvoiceChargesEntity>(memoSalesInvoiceService.findChargesById(id), HttpStatus.OK);
    }

    @PostMapping("/charge/save")
    public ResponseEntity<JewResponse> saveCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesInvoiceChargesEntity entity = memoSalesInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/modify")
    public ResponseEntity<JewResponse> updateCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesInvoiceChargesEntity entity = memoSalesInvoiceService.saveCharges(invoiceChargesDto);
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
            MemoSalesInvoiceChargesEntity entity = memoSalesInvoiceService.findChargesById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            memoSalesInvoiceService.removeCharges(entity);
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

    @PostMapping("/saveall")
    public ResponseEntity<JewResponse> saveAll(@RequestBody InvoiceRequestPayload salesInvoicePayload) {
        JewResponse response = new JewResponse();
        try {
            MemoSalesInvoiceMainEntity entity = memoSalesInvoiceService.createSalesInvoice(salesInvoicePayload);
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
        return new ResponseEntity<List<ProductEntity>>(this.memoSalesInvoiceService.getProductList(searchKey), HttpStatus.OK);
    }

    @PostMapping("/confirm/{memoinvoiceid}")
    public ResponseEntity<JewResponse> confirmSales(@PathVariable("memoinvoiceid") Integer memoInvoiceId) {
        JewResponse response = new JewResponse();
        try {
            String confirmNo = memoSalesInvoiceService.confirmSales(memoInvoiceId);
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
            String confirmNo = memoSalesInvoiceService.confirmPartialSales(memoInvoiceId, invoiceDetailDtoList);
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
            memoSalesInvoiceService.partialReturnMemoSales(memoInvoiceId, invoiceDetailDtoList);
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
            memoSalesInvoiceService.returnMemoSales(memoInvoiceId);
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

    @GetMapping("/getsalesdetailswithstock/{salesinvoiceid}")
    public ResponseEntity<List<MemoSalesInvoiceDetailEntity>> getSalesInvoiceWithStock(@PathVariable("salesinvoiceid") Integer salesInvoiceId) {
        return new ResponseEntity<List<MemoSalesInvoiceDetailEntity>>(this.memoSalesInvoiceService.findDetailByIdWithStock(salesInvoiceId), HttpStatus.OK);
    }

    @GetMapping("/getpendingreturnlistbyparty/{customerid}/{currency}")
    public ResponseEntity<List<MemoSalesInvoiceDetailEntity>> getPendingReturnListByParty(
            @PathVariable("customerid") Integer partyId,
            @PathVariable("currency") String currency) {
        return new ResponseEntity<List<MemoSalesInvoiceDetailEntity>>(this.memoSalesInvoiceService.findPendingReturnList(partyId, currency), HttpStatus.OK);
    }

    @GetMapping("/getpendingreturnlistproduct/{customerid}/{currency}/{productid}")
    public ResponseEntity<List<MemoSalesInvoiceDetailEntity>> getPendingReturnListByProduct(
            @PathVariable("customerid") Integer partyId,
            @PathVariable("currency") String currency,
            @PathVariable("productid") Integer productId) {
        return new ResponseEntity<List<MemoSalesInvoiceDetailEntity>>(this.memoSalesInvoiceService.findPendingReturnListByProduct(partyId, currency, productId), HttpStatus.OK);
    }


    @PutMapping("/changestatus/{id}")
    public ResponseEntity<JewResponse> changestatus(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            memoSalesInvoiceService.changeStatus(id);
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
            List<MemoSalesInvoiceMainEntity> entities = memoSalesInvoiceService.findAllEntitiesByIds(ids);
            List<MemoSalesInvoiceDetailEntity> detailEntities = memoSalesInvoiceService.findAllDetailByMainIds(entities.stream().map(MemoSalesInvoiceMainEntity::getId).collect(Collectors.toList()));
            List<MemoSalesInvoiceChargesEntity> chargesEntities = memoSalesInvoiceService.findAllChargesByMainIds(entities.stream().map(MemoSalesInvoiceMainEntity::getId).collect(Collectors.toList()));
            memoSalesInvoiceService.removeAll(entities.stream().map(MemoSalesInvoiceMainEntity::getId).collect(Collectors.toList()),
                    detailEntities.stream().map(MemoSalesInvoiceDetailEntity::getId).collect(Collectors.toList()),
                    chargesEntities.stream().map(MemoSalesInvoiceChargesEntity::getId).collect(Collectors.toList()));
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
            List<MemoSalesInvoiceMainEntity> entities = memoSalesInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(1);
            });
            memoSalesInvoiceService.lock(entities);
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
            List<MemoSalesInvoiceMainEntity> entities = memoSalesInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(0);
            });
            memoSalesInvoiceService.unlock(entities);
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
