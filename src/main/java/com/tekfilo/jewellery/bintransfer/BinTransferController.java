package com.tekfilo.jewellery.bintransfer;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.bintransfer.dto.BinTransferCommonDto;
import com.tekfilo.jewellery.bintransfer.dto.BinTransferDetailDto;
import com.tekfilo.jewellery.bintransfer.dto.BinTransferDto;
import com.tekfilo.jewellery.bintransfer.entity.BinTransferDetailEntity;
import com.tekfilo.jewellery.bintransfer.entity.BinTransferEntity;
import com.tekfilo.jewellery.bintransfer.service.BinTransferService;
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

@Slf4j
@RestController
@RequestMapping("/jew/bintransfer")
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
    public ResponseEntity<JewResponse> save(@RequestBody BinTransferCommonDto input) {
        JewResponse response = new JewResponse();
        try {
            binTransferService.save(input);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception e) {
            response.setLangStatus("save_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
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
    public ResponseEntity<JewResponse> delete(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            this.binTransferService.deleteAll(id);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus("remove_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception e) {
            response.setLangStatus("remove_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/searchproductbykey/{searchkey}")
    public ResponseEntity<List<ProductEntity>> getProductByKey(@PathVariable("searchkey") String searchKey) {
        return new ResponseEntity<List<ProductEntity>>(this.binTransferService.getProductList(searchKey), HttpStatus.OK);
    }


    @PostMapping("/savemain")
    public ResponseEntity<JewResponse> saveMainMixing(@RequestBody BinTransferDto binTransferDto) {
        JewResponse response = new JewResponse();
        try {
            BinTransferEntity entity = this.binTransferService.saveMain(binTransferDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception e) {
            response.setLangStatus("save_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifymain")
    public ResponseEntity<JewResponse> updateMain(@RequestBody BinTransferDto binTransferDto) {
        JewResponse response = new JewResponse();
        try {
            this.binTransferService.saveMain(binTransferDto);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception e) {
            response.setLangStatus("modify_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/savedetail")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody BinTransferDetailDto binTransferDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BinTransferDetailEntity entity = this.binTransferService.saveDetail(binTransferDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception e) {
            response.setLangStatus("save_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/updatedetail")
    public ResponseEntity<JewResponse> updateDetail(@RequestBody BinTransferDetailDto binTransferDetailDto) {
        JewResponse response = new JewResponse();
        try {
            BinTransferDetailEntity entity = this.binTransferService.saveDetail(binTransferDetailDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception e) {
            response.setLangStatus("modify_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/deletedetail/{id}")
    public ResponseEntity<JewResponse> deleteDetail(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            BinTransferDetailEntity entity = binTransferService.findDetailById(id);
            entity.setIsDeleted(1);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            binTransferService.deleteDetail(entity);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.SUCCESS_STATUS);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception e) {
            response.setLangStatus("modify_101");
            response.setStatus(101);
            response.setMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<JewResponse> remove(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            binTransferService.deleteAllSelected(ids);
            response.setId(null);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
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
            List<BinTransferEntity> entities = binTransferService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            binTransferService.lock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
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
            List<BinTransferEntity> entities = binTransferService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            binTransferService.unlock(entities);
            response.setId(null);
            response.setStatus(MessageConstants.SUCCESS_STATUS);
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
