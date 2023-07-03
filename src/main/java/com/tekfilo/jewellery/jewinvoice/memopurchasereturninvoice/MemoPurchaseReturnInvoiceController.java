package com.tekfilo.jewellery.jewinvoice.memopurchasereturninvoice;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchasereturninvoice.service.MemoPurchaseReturnInvoiceService;
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
@RequestMapping("/jew/invoice/memo/purchase/return")
public class MemoPurchaseReturnInvoiceController {

    @Autowired
    MemoPurchaseReturnInvoiceService memoPurchaseReturnInvoiceService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<MemoPurchaseReturnInvoiceMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<MemoPurchaseReturnInvoiceMainEntity>>
                (memoPurchaseReturnInvoiceService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<MemoPurchaseReturnInvoiceMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoPurchaseReturnInvoiceMainEntity>(memoPurchaseReturnInvoiceService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody InvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseReturnInvoiceMainEntity entity = memoPurchaseReturnInvoiceService.save(invoiceMainDto);
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

    @PutMapping("/modify")
    public ResponseEntity<JewResponse> update(@RequestBody InvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseReturnInvoiceMainEntity entity = memoPurchaseReturnInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<JewResponse> remove(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseReturnInvoiceMainEntity entity = memoPurchaseReturnInvoiceService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);

            List<MemoPurchaseReturnInvoiceDetailEntity> detailEntities = memoPurchaseReturnInvoiceService.findAllDetail(id);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<MemoPurchaseReturnInvoiceChargesEntity> chargesEntities = memoPurchaseReturnInvoiceService.findAllICharges(id);
            chargesEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });

            memoPurchaseReturnInvoiceService.removeAll(entity, detailEntities, chargesEntities);
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


    // Detail of Purchase Invoice

    @GetMapping("/search/detail/{id}")
    public ResponseEntity<List<MemoPurchaseReturnInvoiceDetailEntity>> findAllDetail(
            @PathVariable("id") Integer id) {
        return new ResponseEntity<List<MemoPurchaseReturnInvoiceDetailEntity>>(memoPurchaseReturnInvoiceService.findAllDetail(id), HttpStatus.OK);
    }

    @GetMapping("/search/detailbyrefinvid/{refinvid}/{refinvtype}")
    public ResponseEntity<List<MemoPurchaseReturnInvoiceDetailEntity>> findAllDetailByReferenceInvoiceIdAndType(
            @PathVariable("refinvid") Integer referenceInvoiceId,
            @PathVariable("refinvtype") String referenceInvoiceType) {
        return new ResponseEntity<List<MemoPurchaseReturnInvoiceDetailEntity>>(memoPurchaseReturnInvoiceService.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType), HttpStatus.OK);
    }

    @GetMapping("/detail/findbyid/{id}")
    public ResponseEntity<MemoPurchaseReturnInvoiceDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoPurchaseReturnInvoiceDetailEntity>(memoPurchaseReturnInvoiceService.findDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseReturnInvoiceDetailEntity entity = memoPurchaseReturnInvoiceService.saveDetail(invoiceDetailDto);
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

    @PostMapping("/detail/saveall")
    public ResponseEntity<JewResponse> saveAllDetail(@RequestBody List<InvoiceDetailDto> invoiceDetailDtoList) {
        JewResponse response = new JewResponse();
        try {
            memoPurchaseReturnInvoiceService.saveAllDetail(invoiceDetailDtoList);
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
            MemoPurchaseReturnInvoiceDetailEntity entity = memoPurchaseReturnInvoiceService.saveDetail(invoiceDetailDto);
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
            MemoPurchaseReturnInvoiceDetailEntity entity = memoPurchaseReturnInvoiceService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            memoPurchaseReturnInvoiceService.removeDetail(entity);
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

    // Charges of Purchase Invoice

    @GetMapping("/search/charge/{id}")
    public ResponseEntity<List<MemoPurchaseReturnInvoiceChargesEntity>> findAllCharges(
            @PathVariable("id") Integer invId) {
        return new ResponseEntity<List<MemoPurchaseReturnInvoiceChargesEntity>>(memoPurchaseReturnInvoiceService.findAllICharges(invId), HttpStatus.OK);
    }


    @GetMapping("/charge/findbyid/{id}")
    public ResponseEntity<MemoPurchaseReturnInvoiceChargesEntity> findChargesById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoPurchaseReturnInvoiceChargesEntity>(memoPurchaseReturnInvoiceService.findChargesById(id), HttpStatus.OK);
    }

    @PostMapping("/charge/save")
    public ResponseEntity<JewResponse> saveCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseReturnInvoiceChargesEntity entity = memoPurchaseReturnInvoiceService.saveCharges(invoiceChargesDto);
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
            MemoPurchaseReturnInvoiceChargesEntity entity = memoPurchaseReturnInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Commodity {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/remove/{id}")
    public ResponseEntity<JewResponse> removeCharges(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseReturnInvoiceChargesEntity entity = memoPurchaseReturnInvoiceService.findChargesById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            memoPurchaseReturnInvoiceService.removeCharges(entity);
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
    public ResponseEntity<JewResponse> saveAll(@RequestBody InvoiceRequestPayload purchaseInvoicePayload) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseReturnInvoiceMainEntity entity = memoPurchaseReturnInvoiceService.createMemoPurchaseReturnInvoice(purchaseInvoicePayload);
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
        return new ResponseEntity<List<ProductEntity>>(this.memoPurchaseReturnInvoiceService.getProductList(searchKey), HttpStatus.OK);
    }

    @PutMapping("/changestatus/{id}")
    public ResponseEntity<JewResponse> changestatus(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            memoPurchaseReturnInvoiceService.changeStatus(id);
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
            List<MemoPurchaseReturnInvoiceMainEntity> entities = memoPurchaseReturnInvoiceService.findAllEntitiesByIds(ids);
            List<MemoPurchaseReturnInvoiceDetailEntity> detailEntities = memoPurchaseReturnInvoiceService.findAllDetailByMainIds(entities.stream().map(MemoPurchaseReturnInvoiceMainEntity::getId).collect(Collectors.toList()));
            List<MemoPurchaseReturnInvoiceChargesEntity> chargesEntities = memoPurchaseReturnInvoiceService.findAllChargesByMainIds(entities.stream().map(MemoPurchaseReturnInvoiceMainEntity::getId).collect(Collectors.toList()));
            memoPurchaseReturnInvoiceService.removeAll(entities.stream().map(MemoPurchaseReturnInvoiceMainEntity::getId).collect(Collectors.toList()),
                    detailEntities.stream().map(MemoPurchaseReturnInvoiceDetailEntity::getId).collect(Collectors.toList()),
                    chargesEntities.stream().map(MemoPurchaseReturnInvoiceChargesEntity::getId).collect(Collectors.toList()));
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
            List<MemoPurchaseReturnInvoiceMainEntity> entities = memoPurchaseReturnInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(1);
            });
            memoPurchaseReturnInvoiceService.lock(entities);
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
            List<MemoPurchaseReturnInvoiceMainEntity> entities = memoPurchaseReturnInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(0);
            });
            memoPurchaseReturnInvoiceService.unlock(entities);
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
