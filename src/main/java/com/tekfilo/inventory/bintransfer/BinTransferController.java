package com.tekfilo.inventory.bintransfer;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.product.ProductEntity;
import com.tekfilo.inventory.util.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inventory/bintransfer")
public class BinTransferController {

    @Autowired
    BinTransferService binTransferService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<BinTransferEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<BinTransferEntity>>(binTransferService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> save(@RequestBody BinTransferCommonDto input) {
        InventoryResponse response = new InventoryResponse();
        try {
            binTransferService.save(input);
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Bin Transfer Created Successfully");
        } catch (Exception e) {
            response.setLangStatus("save_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/findbyid/{id}")
    public ResponseEntity<BinTransferEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<BinTransferEntity>(this.binTransferService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/findalldetail/{mainid}")
    public ResponseEntity<List<BinTransferDetailEntity>> findDetailByMainId(@PathVariable("mainid") Integer mainId) {
        return new ResponseEntity<List<BinTransferDetailEntity>>(binTransferService.findAllDetailByMainId(mainId), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<InventoryResponse> delete(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.binTransferService.deleteAll(id);
            response.setStatus(100);
            response.setLangStatus("remove_100");
            response.setMessage("Bin Transfer Deleted Successfully");
        } catch (Exception e) {
            response.setLangStatus("remove_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/searchproductbykey/{searchkey}")
    public ResponseEntity<List<ProductEntity>> getProductByKey(@PathVariable("searchkey") String searchKey) {
        return new ResponseEntity<List<ProductEntity>>(this.binTransferService.getProductList(searchKey), HttpStatus.OK);
    }


    @PostMapping("/savemain")
    public ResponseEntity<InventoryResponse> saveMainMixing(@RequestBody BinTransferDto binTransferDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            BinTransferEntity entity = this.binTransferService.saveMain(binTransferDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Saved Successfully");
        } catch (Exception e) {
            response.setLangStatus("save_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifymain")
    public ResponseEntity<InventoryResponse> updateMain(@RequestBody BinTransferDto binTransferDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            this.binTransferService.saveMain(binTransferDto);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Main Modified Successfully");
        } catch (Exception e) {
            response.setLangStatus("modify_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/savedetail")
    public ResponseEntity<InventoryResponse> saveDetail(@RequestBody BinTransferDetailDto binTransferDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            BinTransferDetailEntity entity = this.binTransferService.saveDetail(binTransferDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage("Detail Saved Successfully");
        } catch (Exception e) {
            response.setLangStatus("save_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/updatedetail")
    public ResponseEntity<InventoryResponse> updateDetail(@RequestBody BinTransferDetailDto binTransferDetailDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            BinTransferDetailEntity entity = this.binTransferService.saveDetail(binTransferDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Detail Modified Successfully");
        } catch (Exception e) {
            response.setLangStatus("modify_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/deletedetail/{id}")
    public ResponseEntity<InventoryResponse> deleteDetail(@PathVariable("id") Integer id) {
        InventoryResponse response = new InventoryResponse();
        try {
            BinTransferDetailEntity entity = binTransferService.findDetailById(id);
            entity.setIsDeleted(1);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            binTransferService.deleteDetail(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Detail Modified Successfully");
        } catch (Exception e) {
            response.setLangStatus("modify_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> remove(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            binTransferService.deleteAllSelected(ids);
            response.setId(null);
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


    @PutMapping("/lock")
    public ResponseEntity<InventoryResponse> lock(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<BinTransferEntity> entities = binTransferService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            binTransferService.lock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage("Locked Successfully");
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
            List<BinTransferEntity> entities = binTransferService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            binTransferService.unlock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage("UnLocked Successfully");
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying  {}" + exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }
}
