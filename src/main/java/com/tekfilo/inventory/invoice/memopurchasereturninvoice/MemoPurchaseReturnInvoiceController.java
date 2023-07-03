package com.tekfilo.inventory.invoice.memopurchasereturninvoice;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.entity.MemoPurchaseReturnInvoiceMainEntity;
import com.tekfilo.inventory.invoice.memopurchasereturninvoice.service.MemoPurchaseReturnInvoiceService;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.util.InventoryResponse;
import com.tekfilo.inventory.util.MessageConstants;
import com.tekfilo.inventory.util.TekfiloUtils;
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
@RequestMapping("/inventory/invoice/memo/purchase/return")
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
    public ResponseEntity<InventoryResponse> save(@RequestBody InvoiceMainDto invoiceMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            MemoPurchaseReturnInvoiceMainEntity entity = memoPurchaseReturnInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<InventoryResponse> update(@RequestBody InvoiceMainDto invoiceMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            MemoPurchaseReturnInvoiceMainEntity entity = memoPurchaseReturnInvoiceService.save(invoiceMainDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<InventoryResponse> remove(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
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
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
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
    public ResponseEntity<InventoryResponse> saveDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            MemoPurchaseReturnInvoiceDetailEntity entity = memoPurchaseReturnInvoiceService.saveDetail(invoiceDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/detail/saveall")
    public ResponseEntity<InventoryResponse> saveAllDetail(@RequestBody List<InvoiceDetailDto> invoiceDetailDtoList) {
        InventoryResponse response = new InventoryResponse();
        try {
            memoPurchaseReturnInvoiceService.saveAllDetail(invoiceDetailDtoList);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/modify")
    public ResponseEntity<InventoryResponse> updateDetail(@RequestBody InvoiceDetailDto invoiceDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            MemoPurchaseReturnInvoiceDetailEntity entity = memoPurchaseReturnInvoiceService.saveDetail(invoiceDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/remove/{id}")
    public ResponseEntity<InventoryResponse> removeDetail(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
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
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
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
    public ResponseEntity<InventoryResponse> saveCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            MemoPurchaseReturnInvoiceChargesEntity entity = memoPurchaseReturnInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/modify")
    public ResponseEntity<InventoryResponse> updateCharges(@RequestBody InvoiceChargesDto invoiceChargesDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            MemoPurchaseReturnInvoiceChargesEntity entity = memoPurchaseReturnInvoiceService.saveCharges(invoiceChargesDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/remove/{id}")
    public ResponseEntity<InventoryResponse> removeCharges(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
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
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/saveall")
    public ResponseEntity<InventoryResponse> saveAll(@RequestBody InvoiceRequestPayload purchaseInvoicePayload) {
        InventoryResponse response = new InventoryResponse();
        try {
            MemoPurchaseReturnInvoiceMainEntity entity = memoPurchaseReturnInvoiceService.createMemoPurchaseReturnInvoice(purchaseInvoicePayload);
            response.setId(entity.getId());
            response.setVoucherTypeNo(entity.getInvoiceTypeNo());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/searchproductbykey/{searchkey}")
    public ResponseEntity<List<ProductEntity>> getProductByKey(@PathVariable("searchkey") String searchKey) {
        return new ResponseEntity<List<ProductEntity>>(this.memoPurchaseReturnInvoiceService.getProductList(searchKey), HttpStatus.OK);
    }

    @PutMapping("/changestatus/{id}")
    public ResponseEntity<InventoryResponse> changestatus(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            memoPurchaseReturnInvoiceService.changeStatus(id);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("modify_100");
            response.setMessage("Successfully Approved");
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> removeAll(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
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
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<InventoryResponse> lock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
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
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<InventoryResponse> unlock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
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
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }
}
