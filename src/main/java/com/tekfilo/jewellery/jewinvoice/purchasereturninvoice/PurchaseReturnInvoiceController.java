package com.tekfilo.jewellery.jewinvoice.purchasereturninvoice;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.entity.PurchaseReturnInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.purchasereturninvoice.service.PurchaseReturnInvoiceService;
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
@RequestMapping("/jew/invoice/purchase/return")
public class PurchaseReturnInvoiceController {

    @Autowired
    PurchaseReturnInvoiceService purchaseReturnInvoiceService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<PurchaseReturnInvoiceMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<PurchaseReturnInvoiceMainEntity>>
                (purchaseReturnInvoiceService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<PurchaseReturnInvoiceMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<PurchaseReturnInvoiceMainEntity>(purchaseReturnInvoiceService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody InvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseReturnInvoiceMainEntity entity = purchaseReturnInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<JewResponse> update(@RequestBody InvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseReturnInvoiceMainEntity entity = purchaseReturnInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<JewResponse> remove(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            PurchaseReturnInvoiceMainEntity entity = purchaseReturnInvoiceService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);

            List<PurchaseReturnInvoiceDetailEntity> detailEntities = purchaseReturnInvoiceService.findAllDetail(id);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<PurchaseReturnInvoiceChargesEntity> chargesEntities = purchaseReturnInvoiceService.findAllICharges(id);
            chargesEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });

            purchaseReturnInvoiceService.removeAll(entity, detailEntities, chargesEntities);

            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    // Detail of Purchase Invoice

    @GetMapping("/search/detail/{id}")
    public ResponseEntity<List<PurchaseReturnInvoiceDetailEntity>> findAllDetail(
            @PathVariable("id") Integer id) {
        return new ResponseEntity<List<PurchaseReturnInvoiceDetailEntity>>(purchaseReturnInvoiceService.findAllDetail(id), HttpStatus.OK);
    }

    @GetMapping("/search/detailbyrefinvid/{refinvid}/{refinvtype}")
    public ResponseEntity<List<PurchaseReturnInvoiceDetailEntity>> findAllDetailByReferenceInvoiceIdAndType(
            @PathVariable("refinvid") Integer referenceInvoiceId,
            @PathVariable("refinvtype") String referenceInvoiceType) {
        return new ResponseEntity<List<PurchaseReturnInvoiceDetailEntity>>(purchaseReturnInvoiceService.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType), HttpStatus.OK);
    }

    @GetMapping("/detail/findbyid/{id}")
    public ResponseEntity<PurchaseReturnInvoiceDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<PurchaseReturnInvoiceDetailEntity>(purchaseReturnInvoiceService.findDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseReturnInvoiceDetailEntity entity = purchaseReturnInvoiceService.saveDetail(invoiceDetailDto);
            response.setId(entity.getId());
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

    @PostMapping("/detail/saveall")
    public ResponseEntity<JewResponse> saveAllDetail(@RequestBody List<InvoiceDetailDto> invoiceDetailDtoList) {
        JewResponse response = new JewResponse();
        try {
            purchaseReturnInvoiceService.saveAllDetail(invoiceDetailDtoList);
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
            PurchaseReturnInvoiceDetailEntity entity = purchaseReturnInvoiceService.saveDetail(invoiceDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/remove/{id}")
    public ResponseEntity<JewResponse> removeDetail(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            PurchaseReturnInvoiceDetailEntity entity = purchaseReturnInvoiceService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            purchaseReturnInvoiceService.removeDetail(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("remove_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    // Charges of Purchase Invoice

    @GetMapping("/search/charge/{id}")
    public ResponseEntity<List<PurchaseReturnInvoiceChargesEntity>> findAllCharges(
            @PathVariable("id") Integer invId) {
        return new ResponseEntity<List<PurchaseReturnInvoiceChargesEntity>>(purchaseReturnInvoiceService.findAllICharges(invId), HttpStatus.OK);
    }


    @GetMapping("/charge/findbyid/{id}")
    public ResponseEntity<PurchaseReturnInvoiceChargesEntity> findChargesById(@PathVariable("id") Integer id) {
        return new ResponseEntity<PurchaseReturnInvoiceChargesEntity>(purchaseReturnInvoiceService.findChargesById(id), HttpStatus.OK);
    }

    @PostMapping("/charge/save")
    public ResponseEntity<JewResponse> saveCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseReturnInvoiceChargesEntity entity = purchaseReturnInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/modify")
    public ResponseEntity<JewResponse> updateCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        JewResponse response = new JewResponse();
        try {
            PurchaseReturnInvoiceChargesEntity entity = purchaseReturnInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/remove/{id}")
    public ResponseEntity<JewResponse> removeCharges(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            PurchaseReturnInvoiceChargesEntity entity = purchaseReturnInvoiceService.findChargesById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            purchaseReturnInvoiceService.removeCharges(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/saveall")
    public ResponseEntity<JewResponse> saveAll(@RequestBody InvoiceRequestPayload purchaseInvoicePayload) {
        JewResponse response = new JewResponse();
        try {
            PurchaseReturnInvoiceMainEntity entity = purchaseReturnInvoiceService.createPurchaseInvoice(purchaseInvoicePayload);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception e) {
            response.setId(null);
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            log.error("Exception raised while modifying " + e.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/searchproductbykey/{searchkey}")
    public ResponseEntity<List<ProductEntity>> getProductByKey(@PathVariable("searchkey") String searchKey) {
        return new ResponseEntity<List<ProductEntity>>(this.purchaseReturnInvoiceService.getProductList(searchKey), HttpStatus.OK);
    }

    @PutMapping("/changestatus/{id}")
    public ResponseEntity<JewResponse> changestatus(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            purchaseReturnInvoiceService.changeStatus(id);
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
            List<PurchaseReturnInvoiceMainEntity> entities = purchaseReturnInvoiceService.findAllEntitiesByIds(ids);
            List<PurchaseReturnInvoiceDetailEntity> detailEntities = purchaseReturnInvoiceService.findAllDetailByMainIds(entities.stream().map(PurchaseReturnInvoiceMainEntity::getId).collect(Collectors.toList()));
            List<PurchaseReturnInvoiceChargesEntity> chargesEntities = purchaseReturnInvoiceService.findAllChargesByMainIds(entities.stream().map(PurchaseReturnInvoiceMainEntity::getId).collect(Collectors.toList()));
            purchaseReturnInvoiceService.removeAll(entities.stream().map(PurchaseReturnInvoiceMainEntity::getId).collect(Collectors.toList()),
                    detailEntities.stream().map(PurchaseReturnInvoiceDetailEntity::getId).collect(Collectors.toList()),
                    chargesEntities.stream().map(PurchaseReturnInvoiceChargesEntity::getId).collect(Collectors.toList()));
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
            List<PurchaseReturnInvoiceMainEntity> entities = purchaseReturnInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(1);
            });
            purchaseReturnInvoiceService.lock(entities);
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
            List<PurchaseReturnInvoiceMainEntity> entities = purchaseReturnInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(0);
            });
            purchaseReturnInvoiceService.unlock(entities);
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
