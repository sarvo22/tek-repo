package com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.service.MemoPurchaseInvoiceService;
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

@Slf4j
@RestController
@RequestMapping("/jew/invoice/memo/purchase")
public class MemoPurchaseInvoiceController {

    @Autowired
    MemoPurchaseInvoiceService memoPurchaseInvoiceService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<MemoPurchaseInvoiceMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<MemoPurchaseInvoiceMainEntity>>
                (memoPurchaseInvoiceService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<MemoPurchaseInvoiceMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoPurchaseInvoiceMainEntity>(memoPurchaseInvoiceService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody InvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseInvoiceMainEntity entity = memoPurchaseInvoiceService.save(invoiceMainDto);
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
            MemoPurchaseInvoiceMainEntity entity = memoPurchaseInvoiceService.save(invoiceMainDto);
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
            MemoPurchaseInvoiceMainEntity entity = memoPurchaseInvoiceService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);

            List<MemoPurchaseInvoiceDetailEntity> detailEntities = memoPurchaseInvoiceService.findAllDetail(id);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<MemoPurchaseInvoiceChargesEntity> chargesEntities = memoPurchaseInvoiceService.findAllICharges(id);
            chargesEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });

            memoPurchaseInvoiceService.removeAll(entity, detailEntities, chargesEntities);

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


    @GetMapping("/search/detail/{id}")
    public ResponseEntity<List<MemoPurchaseInvoiceDetailEntity>> findAllDetail(
            @PathVariable("id") Integer id) {
        return new ResponseEntity<List<MemoPurchaseInvoiceDetailEntity>>(memoPurchaseInvoiceService.findAllDetail(id), HttpStatus.OK);
    }


    @GetMapping("/detail/findbyid/{id}")
    public ResponseEntity<MemoPurchaseInvoiceDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoPurchaseInvoiceDetailEntity>(memoPurchaseInvoiceService.findDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseInvoiceDetailEntity entity = memoPurchaseInvoiceService.saveDetail(invoiceDetailDto);
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
            MemoPurchaseInvoiceDetailEntity entity = memoPurchaseInvoiceService.saveDetail(invoiceDetailDto);
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

    @PutMapping("/detail/remove/{id}")
    public ResponseEntity<JewResponse> removeDetail(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseInvoiceDetailEntity entity = memoPurchaseInvoiceService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            memoPurchaseInvoiceService.removeDetail(entity);
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


    @GetMapping("/search/charge/{id}")
    public ResponseEntity<List<MemoPurchaseInvoiceChargesEntity>> findAllCharges(
            @PathVariable("id") Integer invId) {
        return new ResponseEntity<List<MemoPurchaseInvoiceChargesEntity>>(memoPurchaseInvoiceService.findAllICharges(invId), HttpStatus.OK);
    }


    @GetMapping("/charge/findbyid/{id}")
    public ResponseEntity<MemoPurchaseInvoiceChargesEntity> findChargesById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MemoPurchaseInvoiceChargesEntity>(memoPurchaseInvoiceService.findChargesById(id), HttpStatus.OK);
    }

    @PostMapping("/charge/save")
    public ResponseEntity<JewResponse> saveCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        JewResponse response = new JewResponse();
        try {
            MemoPurchaseInvoiceChargesEntity entity = memoPurchaseInvoiceService.saveCharges(invoiceChargesDto);
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
            MemoPurchaseInvoiceChargesEntity entity = memoPurchaseInvoiceService.saveCharges(invoiceChargesDto);
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
            MemoPurchaseInvoiceChargesEntity entity = memoPurchaseInvoiceService.findChargesById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            memoPurchaseInvoiceService.removeCharges(entity);
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
            MemoPurchaseInvoiceMainEntity entity = memoPurchaseInvoiceService.createPurchaseInvoice(purchaseInvoicePayload);
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

    @PutMapping("/changestatus/{id}")
    public ResponseEntity<JewResponse> changestatus(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            memoPurchaseInvoiceService.changeStatus(id);
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

    @GetMapping("/searchproductbykey/{searchkey}")
    public ResponseEntity<List<ProductEntity>> getProductByKey(@PathVariable("searchkey") String searchKey) {
        return new ResponseEntity<List<ProductEntity>>(this.memoPurchaseInvoiceService.getProductList(searchKey), HttpStatus.OK);
    }

    @GetMapping("/getmemopurchasedetailswithstock/{purchaseinvoiceid}")
    public ResponseEntity<List<MemoPurchaseInvoiceDetailEntity>> getPurchaseInvoiceWithStock(@PathVariable("purchaseinvoiceid") Integer purchaseInvoiceId) {
        return new ResponseEntity<List<MemoPurchaseInvoiceDetailEntity>>(this.memoPurchaseInvoiceService.findDetailByIdWithStock(purchaseInvoiceId), HttpStatus.OK);
    }

    @GetMapping("/getpendingreturnlistbyparty/{supplierid}/{currency}")
    public ResponseEntity<List<MemoPurchaseInvoiceDetailEntity>> getPendingReturnListByParty(
            @PathVariable("supplierid") Integer partyId,
            @PathVariable("currency") String currency) {
        return new ResponseEntity<List<MemoPurchaseInvoiceDetailEntity>>(this.memoPurchaseInvoiceService.findPendingReturnList(partyId, currency), HttpStatus.OK);
    }

    @GetMapping("/getpendingreturnlistproduct/{customerid}/{currency}/{productid}")
    public ResponseEntity<List<MemoPurchaseInvoiceDetailEntity>> getPendingReturnListByProduct(
            @PathVariable("customerid") Integer partyId,
            @PathVariable("currency") String currency,
            @PathVariable("productid") Integer productId) {
        return new ResponseEntity<List<MemoPurchaseInvoiceDetailEntity>>(this.memoPurchaseInvoiceService.findPendingReturnListByProduct(partyId, currency, productId), HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<JewResponse> remove(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<MemoPurchaseInvoiceMainEntity> entityList = memoPurchaseInvoiceService.findAllMainByMainIds(ids);
            entityList.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });

            List<MemoPurchaseInvoiceDetailEntity> detailEntities = memoPurchaseInvoiceService.findAllDetailByMainIds(ids);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<MemoPurchaseInvoiceChargesEntity> chargesEntities = memoPurchaseInvoiceService.findAllIChargesByMainIds(ids);
            chargesEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });
            memoPurchaseInvoiceService.removeAll(entityList, detailEntities, chargesEntities);
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
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<JewResponse> lock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<MemoPurchaseInvoiceMainEntity> entities = memoPurchaseInvoiceService.findAllMainByMainIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            memoPurchaseInvoiceService.lock(entities);
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
            List<MemoPurchaseInvoiceMainEntity> entities = memoPurchaseInvoiceService.findAllMainByMainIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            memoPurchaseInvoiceService.unlock(entities);
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
