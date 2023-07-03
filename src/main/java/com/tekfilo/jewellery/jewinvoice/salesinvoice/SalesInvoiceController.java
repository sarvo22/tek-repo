package com.tekfilo.jewellery.jewinvoice.salesinvoice;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceMainEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.service.SalesInvoiceService;
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
@RequestMapping("/jew/invoice/sales")
public class SalesInvoiceController {

    @Autowired
    SalesInvoiceService salesInvoiceService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<SalesInvoiceMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<SalesInvoiceMainEntity>>
                (salesInvoiceService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<SalesInvoiceMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SalesInvoiceMainEntity>(salesInvoiceService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody InvoiceMainDto invoiceMainDto) {
        JewResponse response = new JewResponse();
        try {
            SalesInvoiceMainEntity entity = salesInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
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
            SalesInvoiceMainEntity entity = salesInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
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
            SalesInvoiceMainEntity entity = salesInvoiceService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);

            List<SalesInvoiceDetailEntity> detailEntities = salesInvoiceService.findAllDetail(id);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<SalesInvoiceChargesEntity> chargesEntities = salesInvoiceService.findAllICharges(id);
            chargesEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });

            salesInvoiceService.removeAll(entity, detailEntities, chargesEntities);

            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    // Detail of Sales Invoice

    @GetMapping("/search/detail/{id}")
    public ResponseEntity<List<SalesInvoiceDetailEntity>> findAllDetail(
            @PathVariable("id") Integer id) {
        return new ResponseEntity<List<SalesInvoiceDetailEntity>>(salesInvoiceService.findAllDetail(id), HttpStatus.OK);
    }


    @GetMapping("/detail/findbyid/{id}")
    public ResponseEntity<SalesInvoiceDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SalesInvoiceDetailEntity>(salesInvoiceService.findDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        JewResponse response = new JewResponse();
        try {
            SalesInvoiceDetailEntity entity = salesInvoiceService.saveDetail(invoiceDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
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
            SalesInvoiceDetailEntity entity = salesInvoiceService.saveDetail(invoiceDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
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
            SalesInvoiceDetailEntity entity = salesInvoiceService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            salesInvoiceService.removeDetail(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Removed Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    // Charges of Sales Invoice

    @GetMapping("/search/charge/{id}")
    public ResponseEntity<List<SalesInvoiceChargesEntity>> findAllCharges(
            @PathVariable("id") Integer invId) {
        return new ResponseEntity<List<SalesInvoiceChargesEntity>>(salesInvoiceService.findAllICharges(invId), HttpStatus.OK);
    }

    @GetMapping("/search/detailbyrefinvid/{refinvid}/{refinvtype}")
    public ResponseEntity<List<SalesInvoiceDetailEntity>> findAllDetailByReferenceInvoiceIdAndType(
            @PathVariable("refinvid") Integer referenceInvoiceId,
            @PathVariable("refinvtype") String referenceInvoiceType) {
        return new ResponseEntity<List<SalesInvoiceDetailEntity>>(salesInvoiceService.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType), HttpStatus.OK);
    }

    @GetMapping("/charge/findbyid/{id}")
    public ResponseEntity<SalesInvoiceChargesEntity> findChargesById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SalesInvoiceChargesEntity>(salesInvoiceService.findChargesById(id), HttpStatus.OK);
    }

    @PostMapping("/charge/save")
    public ResponseEntity<JewResponse> saveCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        JewResponse response = new JewResponse();
        try {
            SalesInvoiceChargesEntity entity = salesInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
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
            SalesInvoiceChargesEntity entity = salesInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
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
            SalesInvoiceChargesEntity entity = salesInvoiceService.findChargesById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            salesInvoiceService.removeCharges(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Modified Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying " + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/saveall")
    public ResponseEntity<JewResponse> saveAll(@RequestBody InvoiceRequestPayload salesInvoicePayload) {
        JewResponse response = new JewResponse();
        try {
            SalesInvoiceMainEntity entity = salesInvoiceService.createSalesInvoice(salesInvoicePayload);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Invoice Saved Successfully");
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
        return new ResponseEntity<List<ProductEntity>>(this.salesInvoiceService.getProductList(searchKey), HttpStatus.OK);
    }

    @GetMapping("/getdetailbyproduct/{productid}")
    public ResponseEntity<List<SalesInvoiceDetailEntity>> getDetailByProduct(@PathVariable("productid") Integer productId) {
        return new ResponseEntity<List<SalesInvoiceDetailEntity>>(this.salesInvoiceService.findDetailByProductId(productId), HttpStatus.OK);
    }


    @PutMapping("/changestatus/{id}")
    public ResponseEntity<JewResponse> changestatus(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            salesInvoiceService.changeStatus(id);
            response.setStatus(100);
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

    @GetMapping("/getsalesdetailswithstock/{salesinvoiceid}")
    public ResponseEntity<List<SalesInvoiceDetailEntity>> getSalesInvoiceWithStock(@PathVariable("salesinvoiceid") Integer salesInvoiceId) {
        return new ResponseEntity<List<SalesInvoiceDetailEntity>>(this.salesInvoiceService.findDetailByIdWithStock(salesInvoiceId), HttpStatus.OK);
    }

    @GetMapping("/getpendingreturnlistbyparty/{customerid}/{currency}")
    public ResponseEntity<List<SalesInvoiceDetailEntity>> getPendingReturnListByParty(
            @PathVariable("customerid") Integer partyId,
            @PathVariable("currency") String currency) {
        return new ResponseEntity<List<SalesInvoiceDetailEntity>>(this.salesInvoiceService.findPendingReturnList(partyId, currency), HttpStatus.OK);
    }

    @GetMapping("/getpendingreturnlistproduct/{customerid}/{currency}/{productid}")
    public ResponseEntity<List<SalesInvoiceDetailEntity>> getPendingReturnListByProduct(
            @PathVariable("customerid") Integer partyId,
            @PathVariable("currency") String currency,
            @PathVariable("productid") Integer productId) {
        return new ResponseEntity<List<SalesInvoiceDetailEntity>>(this.salesInvoiceService.findPendingReturnListByProduct(partyId, currency, productId), HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<JewResponse> removeAll(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<SalesInvoiceMainEntity> entities = salesInvoiceService.findAllEntitiesByIds(ids);
            List<SalesInvoiceDetailEntity> detailEntities = salesInvoiceService.findAllDetailByMainIds(entities.stream().map(SalesInvoiceMainEntity::getId).collect(Collectors.toList()));
            List<SalesInvoiceChargesEntity> chargesEntities = salesInvoiceService.findAllChargesByMainIds(entities.stream().map(SalesInvoiceMainEntity::getId).collect(Collectors.toList()));
            salesInvoiceService.removeAll(entities.stream().map(SalesInvoiceMainEntity::getId).collect(Collectors.toList()),
                    detailEntities.stream().map(SalesInvoiceDetailEntity::getId).collect(Collectors.toList()),
                    chargesEntities.stream().map(SalesInvoiceChargesEntity::getId).collect(Collectors.toList()));
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
            List<SalesInvoiceMainEntity> entities = salesInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(1);
            });
            salesInvoiceService.lock(entities);
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
            List<SalesInvoiceMainEntity> entities = salesInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(0);
            });
            salesInvoiceService.unlock(entities);
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
