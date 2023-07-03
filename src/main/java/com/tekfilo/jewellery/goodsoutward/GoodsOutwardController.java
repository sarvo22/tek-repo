package com.tekfilo.jewellery.goodsoutward;


import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardChargesDto;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardDetailDto;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardMainDto;
import com.tekfilo.jewellery.goodsoutward.dto.GoodsOutwardRequestPayload;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardChargesEntity;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardDetailEntity;
import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardMainEntity;
import com.tekfilo.jewellery.goodsoutward.service.GoodsOutwardService;
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
@RequestMapping("/jew/goodsoutward")
public class GoodsOutwardController {

    @Autowired
    GoodsOutwardService goodsOutwardService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<GoodsOutwardMainEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClauses) {
        return new ResponseEntity<Page<GoodsOutwardMainEntity>>
                (goodsOutwardService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClauses), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<GoodsOutwardMainEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<GoodsOutwardMainEntity>(goodsOutwardService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody GoodsOutwardMainDto goodsOutwardMainDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsOutwardMainEntity entity = goodsOutwardService.save(goodsOutwardMainDto);
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
    public ResponseEntity<JewResponse> update(@RequestBody GoodsOutwardMainDto goodsOutwardMainDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsOutwardMainEntity entity = goodsOutwardService.save(goodsOutwardMainDto);
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
            GoodsOutwardMainEntity entity = goodsOutwardService.findById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            goodsOutwardService.remove(entity);
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


    @GetMapping("/search/detail/{id}")
    public ResponseEntity<List<GoodsOutwardDetailEntity>> findAllDetail(
            @PathVariable("id") Integer id) {
        return new ResponseEntity<List<GoodsOutwardDetailEntity>>(goodsOutwardService.findAllDetail(id), HttpStatus.OK);
    }


    @GetMapping("/detail/findbyid/{id}")
    public ResponseEntity<GoodsOutwardDetailEntity> findDetailById(@PathVariable("id") Integer id) {
        return new ResponseEntity<GoodsOutwardDetailEntity>(goodsOutwardService.findDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    public ResponseEntity<JewResponse> saveDetail(@RequestBody GoodsOutwardDetailDto goodsOutwardDetailDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsOutwardDetailEntity entity = goodsOutwardService.saveDetail(goodsOutwardDetailDto);
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
    public ResponseEntity<JewResponse> updateDetail(@RequestBody GoodsOutwardDetailDto goodsOutwardDetailDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsOutwardDetailEntity entity = goodsOutwardService.saveDetail(goodsOutwardDetailDto);
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
            GoodsOutwardDetailEntity entity = goodsOutwardService.findDetailById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            goodsOutwardService.removeDetail(entity);
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


    @GetMapping("/search/charge/{id}")
    public ResponseEntity<List<GoodsOutwardChargesEntity>> findAllCharges(
            @PathVariable("id") Integer invId) {
        return new ResponseEntity<List<GoodsOutwardChargesEntity>>(goodsOutwardService.findAllICharges(invId), HttpStatus.OK);
    }


    @GetMapping("/charge/findbyid/{id}")
    public ResponseEntity<GoodsOutwardChargesEntity> findChargesById(@PathVariable("id") Integer id) {
        return new ResponseEntity<GoodsOutwardChargesEntity>(goodsOutwardService.findChargesById(id), HttpStatus.OK);
    }

    @PostMapping("/charge/save")
    public ResponseEntity<JewResponse> saveCharges(@RequestBody GoodsOutwardChargesDto goodsOutwardChargesDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsOutwardChargesEntity entity = goodsOutwardService.saveCharges(goodsOutwardChargesDto);
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
    public ResponseEntity<JewResponse> updateCharges(@RequestBody GoodsOutwardChargesDto goodsOutwardChargesDto) {
        JewResponse response = new JewResponse();
        try {
            GoodsOutwardChargesEntity entity = goodsOutwardService.saveCharges(goodsOutwardChargesDto);
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
            GoodsOutwardChargesEntity entity = goodsOutwardService.findChargesById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            goodsOutwardService.removeCharges(entity);
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
    public ResponseEntity<JewResponse> saveAll(@RequestBody GoodsOutwardRequestPayload goodsOutwardRequestPayload) {
        JewResponse response = new JewResponse();
        try {
            GoodsOutwardMainEntity entity = goodsOutwardService.createInvoice(goodsOutwardRequestPayload);
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
        return new ResponseEntity<List<ProductEntity>>(this.goodsOutwardService.getProductList(searchKey), HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<JewResponse> removeAll(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<GoodsOutwardMainEntity> entities = goodsOutwardService.findAllEntitiesByIds(ids);
            List<GoodsOutwardDetailEntity> detailEntities = goodsOutwardService.findAllDetailByMainIds(entities.stream().map(GoodsOutwardMainEntity::getId).collect(Collectors.toList()));
            List<GoodsOutwardChargesEntity> chargesEntities = goodsOutwardService.findAllChargesByMainIds(entities.stream().map(GoodsOutwardMainEntity::getId).collect(Collectors.toList()));
            goodsOutwardService.removeAll(entities.stream().map(GoodsOutwardMainEntity::getId).collect(Collectors.toList()),
                    detailEntities.stream().map(GoodsOutwardDetailEntity::getId).collect(Collectors.toList()),
                    chargesEntities.stream().map(GoodsOutwardChargesEntity::getId).collect(Collectors.toList()));
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
            List<GoodsOutwardMainEntity> entities = goodsOutwardService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(1);
            });
            goodsOutwardService.lock(entities);
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
            List<GoodsOutwardMainEntity> entities = goodsOutwardService.findAllEntitiesByIds(ids);
            entities.stream().forEach(e -> {
                e.setIsLocked(0);
            });
            goodsOutwardService.unlock(entities);
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
