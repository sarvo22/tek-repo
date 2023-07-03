package com.tekfilo.jewellery.goodsinward;


import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardChargesDto;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardDetailDto;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardMainDto;
import com.tekfilo.jewellery.goodsinward.dto.GoodsInwardRequestPayload;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardChargesEntity;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardDetailEntity;
import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardMainEntity;
import com.tekfilo.jewellery.goodsinward.service.GoodsInwardService;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
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
@RequestMapping("/jew/goodsinward")
public class GoodsInwardController {

    @Autowired
    GoodsInwardService goodsInwardService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<GoodsInwardMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<GoodsInwardMainEntity>>
                (goodsInwardService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<GoodsInwardMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<GoodsInwardMainEntity>(goodsInwardService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody GoodsInwardMainDto goodsInwardMainDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsInwardMainEntity entity = goodsInwardService.save(goodsInwardMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
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
    public ResponseEntity<JewResponse> update(@RequestBody GoodsInwardMainDto goodsInwardMainDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsInwardMainEntity entity = goodsInwardService.save(goodsInwardMainDto);
            response.setId(entity.getId());
            response.setStatus(100);
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
            GoodsInwardMainEntity entity = goodsInwardService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            goodsInwardService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(100);
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


    @GetMapping("/search/detail/{id}")
    public ResponseEntity<List<GoodsInwardDetailEntity>> findAllDetail(
            @PathVariable("id") Integer id) {
        return new ResponseEntity<List<GoodsInwardDetailEntity>>(goodsInwardService.findAllDetail(id), HttpStatus.OK);
    }


    @GetMapping("/detail/findbyid/{id}")
    public ResponseEntity<GoodsInwardDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<GoodsInwardDetailEntity>(goodsInwardService.findDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody GoodsInwardDetailDto goodsInwardDetailDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsInwardDetailEntity entity = goodsInwardService.saveDetail(goodsInwardDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
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
    public ResponseEntity<JewResponse> updateDetail(@RequestBody GoodsInwardDetailDto goodsInwardDetailDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsInwardDetailEntity entity = goodsInwardService.saveDetail(goodsInwardDetailDto);
            response.setId(entity.getId());
            response.setStatus(100);
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
            GoodsInwardDetailEntity entity = goodsInwardService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            goodsInwardService.removeDetail(entity);
            response.setId(entity.getId());
            response.setStatus(100);
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


    @GetMapping("/search/charge/{id}")
    public ResponseEntity<List<GoodsInwardChargesEntity>> findAllCharges(
            @PathVariable("id") Integer invId) {
        return new ResponseEntity<List<GoodsInwardChargesEntity>>(goodsInwardService.findAllICharges(invId), HttpStatus.OK);
    }


    @GetMapping("/charge/findbyid/{id}")
    public ResponseEntity<GoodsInwardChargesEntity> findChargesById(@PathVariable("id") Integer id) {
        return new ResponseEntity<GoodsInwardChargesEntity>(goodsInwardService.findChargesById(id), HttpStatus.OK);
    }

    @PostMapping("/charge/save")
    public ResponseEntity<JewResponse> saveCharges(@RequestBody GoodsInwardChargesDto goodsInwardChargesDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsInwardChargesEntity entity = goodsInwardService.saveCharges(goodsInwardChargesDto);
            response.setId(entity.getId());
            response.setStatus(100);
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
    public ResponseEntity<JewResponse> updateCharges(@RequestBody GoodsInwardChargesDto goodsInwardChargesDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsInwardChargesEntity entity = goodsInwardService.saveCharges(goodsInwardChargesDto);
            response.setId(entity.getId());
            response.setStatus(100);
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
            GoodsInwardChargesEntity entity = goodsInwardService.findChargesById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            goodsInwardService.removeCharges(entity);
            response.setId(entity.getId());
            response.setStatus(100);
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
    public ResponseEntity<JewResponse> saveAll(@RequestBody GoodsInwardRequestPayload goodsInwardRequestPayload) {
        JewResponse response = new JewResponse();
        try {
            GoodsInwardMainEntity entity = goodsInwardService.createInvoice(goodsInwardRequestPayload);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
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
        return new ResponseEntity<List<ProductEntity>>(this.goodsInwardService.getProductList(searchKey), HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<JewResponse> removeAll(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<GoodsInwardMainEntity> entities = goodsInwardService.findAllEntitiesByIds(ids);
            List<GoodsInwardDetailEntity> detailEntities = goodsInwardService.findAllDetailByMainIds(entities.stream().map(GoodsInwardMainEntity::getId).collect(Collectors.toList()));
            List<GoodsInwardChargesEntity> chargesEntities = goodsInwardService.findAllChargesByMainIds(entities.stream().map(GoodsInwardMainEntity::getId).collect(Collectors.toList()));
            goodsInwardService.removeAll(entities.stream().map(GoodsInwardMainEntity::getId).collect(Collectors.toList()),
                    detailEntities.stream().map(GoodsInwardDetailEntity::getId).collect(Collectors.toList()),
                    chargesEntities.stream().map(GoodsInwardChargesEntity::getId).collect(Collectors.toList()));
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
            List<GoodsInwardMainEntity> entities = goodsInwardService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(1);
            });
            goodsInwardService.lock(entities);
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
            List<GoodsInwardMainEntity> entities = goodsInwardService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(0);
            });
            goodsInwardService.unlock(entities);
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
