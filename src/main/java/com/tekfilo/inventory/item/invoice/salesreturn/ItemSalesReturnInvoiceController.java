package com.tekfilo.inventory.item.invoice.salesreturn;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.item.ItemEntity;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceChargesDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceDetailDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceMainDto;
import com.tekfilo.inventory.item.invoice.common.ItemInvoiceRequestPayload;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceChargesEntity;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceDetailEntity;
import com.tekfilo.inventory.item.invoice.salesreturn.entity.ItemSalesReturnInvoiceMainEntity;
import com.tekfilo.inventory.item.invoice.salesreturn.service.ItemSalesReturnInvoiceService;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.util.InventoryResponse;
import com.tekfilo.inventory.util.MessageConstants;
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
@RequestMapping("/inventory/item/invoice/sales/return")
public class ItemSalesReturnInvoiceController {

    @Autowired
    ItemSalesReturnInvoiceService itemSalesReturnInvoiceService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<ItemSalesReturnInvoiceMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<ItemSalesReturnInvoiceMainEntity>>
                (itemSalesReturnInvoiceService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<ItemSalesReturnInvoiceMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<ItemSalesReturnInvoiceMainEntity>(itemSalesReturnInvoiceService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> save(@RequestBody ItemInvoiceMainDto invoiceMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceMainEntity entity = itemSalesReturnInvoiceService.save(invoiceMainDto);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<InventoryResponse> update(@RequestBody ItemInvoiceMainDto invoiceMainDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceMainEntity entity = itemSalesReturnInvoiceService.save(invoiceMainDto);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<InventoryResponse> remove(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceMainEntity entity = itemSalesReturnInvoiceService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);

            List<ItemSalesReturnInvoiceDetailEntity> detailEntities = itemSalesReturnInvoiceService.findAllDetail(id);
            detailEntities.stream().forEachOrdered(e -> {
                e.setIsDeleted(1);
                e.setModifiedBy(UserContext.getLoggedInUser());
            });

            List<ItemSalesReturnInvoiceChargesEntity> chargesEntities = itemSalesReturnInvoiceService.findAllICharges(id);
            chargesEntities.stream().forEachOrdered(c -> {
                c.setIsDeleted(1);
                c.setModifiedBy(UserContext.getLoggedInUser());
            });

            itemSalesReturnInvoiceService.removeAll(entity, detailEntities, chargesEntities);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    // Detail of Sales Invoice

    @GetMapping("/search/detail/{id}")
    public ResponseEntity<List<ItemSalesReturnInvoiceDetailEntity>> findAllDetail(
            @PathVariable("id") Integer id) {
        return new ResponseEntity<List<ItemSalesReturnInvoiceDetailEntity>>(itemSalesReturnInvoiceService.findAllDetail(id), HttpStatus.OK);
    }


    @GetMapping("/detail/findbyid/{id}")
    public ResponseEntity<ItemSalesReturnInvoiceDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<ItemSalesReturnInvoiceDetailEntity>(itemSalesReturnInvoiceService.findDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    public ResponseEntity<InventoryResponse> saveDetail(@RequestBody ItemInvoiceDetailDto invoiceDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceDetailEntity entity = itemSalesReturnInvoiceService.saveDetail(invoiceDetailDto);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/detail/saveall")
    public ResponseEntity<InventoryResponse> saveAllDetail(@RequestBody List<ItemInvoiceDetailDto> invoiceDetailDtoList) {
        InventoryResponse response = new InventoryResponse();
        try {
            itemSalesReturnInvoiceService.saveAllDetail(invoiceDetailDtoList);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while save {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/modify")
    public ResponseEntity<InventoryResponse> updateDetail(@RequestBody ItemInvoiceDetailDto invoiceDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceDetailEntity entity = itemSalesReturnInvoiceService.saveDetail(invoiceDetailDto);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/detail/remove/{id}")
    public ResponseEntity<InventoryResponse> removeDetail(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceDetailEntity entity = itemSalesReturnInvoiceService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            itemSalesReturnInvoiceService.removeDetail(entity);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    // Charges of Sales Invoice

    @GetMapping("/search/charge/{id}")
    public ResponseEntity<List<ItemSalesReturnInvoiceChargesEntity>> findAllCharges(
            @PathVariable("id") Integer invId) {
        return new ResponseEntity<List<ItemSalesReturnInvoiceChargesEntity>>(itemSalesReturnInvoiceService.findAllICharges(invId), HttpStatus.OK);
    }


    @GetMapping("/charge/findbyid/{id}")
    public ResponseEntity<ItemSalesReturnInvoiceChargesEntity> findChargesById(@PathVariable("id") Integer id) {
        return new ResponseEntity<ItemSalesReturnInvoiceChargesEntity>(itemSalesReturnInvoiceService.findChargesById(id), HttpStatus.OK);
    }

    @PostMapping("/charge/save")
    public ResponseEntity<InventoryResponse> saveCharges(@RequestBody ItemInvoiceChargesDto invoiceChargesDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceChargesEntity entity = itemSalesReturnInvoiceService.saveCharges(invoiceChargesDto);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/modify")
    public ResponseEntity<InventoryResponse> updateCharges(@RequestBody ItemInvoiceChargesDto invoiceChargesDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceChargesEntity entity = itemSalesReturnInvoiceService.saveCharges(invoiceChargesDto);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/charge/remove/{id}")
    public ResponseEntity<InventoryResponse> removeCharges(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceChargesEntity entity = itemSalesReturnInvoiceService.findChargesById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            itemSalesReturnInvoiceService.removeCharges(entity);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/saveall")
    public ResponseEntity<InventoryResponse> saveAll(@RequestBody ItemInvoiceRequestPayload salesInvoicePayload) {
        InventoryResponse response = new InventoryResponse();
        try {
            ItemSalesReturnInvoiceMainEntity entity = itemSalesReturnInvoiceService.createSalesInvoice(salesInvoicePayload);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/searchproductbykey/{searchkey}")
    public ResponseEntity<List<ItemEntity>> getProductByKey(@PathVariable("searchkey") String searchKey) {
        return new ResponseEntity<List<ItemEntity>>(this.itemSalesReturnInvoiceService.getProductList(searchKey), HttpStatus.OK);
    }

    @PutMapping("/changestatus/{id}")
    public ResponseEntity<InventoryResponse> changestatus(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            itemSalesReturnInvoiceService.changeStatus(id);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Successfully Approved");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/search/detailbyrefinvid/{refinvid}/{refinvtype}")
    public ResponseEntity<List<ItemSalesReturnInvoiceDetailEntity>> findAllDetailByReferenceInvoiceIdAndType(
            @PathVariable("refinvid") Integer referenceInvoiceId,
            @PathVariable("refinvtype") String referenceInvoiceType) {
        return new ResponseEntity<List<ItemSalesReturnInvoiceDetailEntity>>(itemSalesReturnInvoiceService.findAllDetailByReferenceInvoiceIdAndType(referenceInvoiceId, referenceInvoiceType), HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> removeAll(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<ItemSalesReturnInvoiceMainEntity> entities = itemSalesReturnInvoiceService.findAllEntitiesByIds(ids);
            List<ItemSalesReturnInvoiceDetailEntity> detailEntities = itemSalesReturnInvoiceService.findAllDetailByMainIds(entities.stream().map(ItemSalesReturnInvoiceMainEntity::getId).collect(Collectors.toList()));
            List<ItemSalesReturnInvoiceChargesEntity> chargesEntities = itemSalesReturnInvoiceService.findAllChargesByMainIds(entities.stream().map(ItemSalesReturnInvoiceMainEntity::getId).collect(Collectors.toList()));
            itemSalesReturnInvoiceService.removeAll(entities.stream().map(ItemSalesReturnInvoiceMainEntity::getId).collect(Collectors.toList()),
                    detailEntities.stream().map(ItemSalesReturnInvoiceDetailEntity::getId).collect(Collectors.toList()),
                    chargesEntities.stream().map(ItemSalesReturnInvoiceChargesEntity::getId).collect(Collectors.toList()));
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/lock")
    public ResponseEntity<InventoryResponse> lock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<ItemSalesReturnInvoiceMainEntity> entities = itemSalesReturnInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(1);
            });
            itemSalesReturnInvoiceService.lock(entities);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<InventoryResponse> unlock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<ItemSalesReturnInvoiceMainEntity> entities = itemSalesReturnInvoiceService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(0);
            });
            itemSalesReturnInvoiceService.unlock(entities);
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
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }
}
