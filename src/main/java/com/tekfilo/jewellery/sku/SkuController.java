package com.tekfilo.jewellery.sku;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.sku.dto.*;
import com.tekfilo.jewellery.sku.entity.*;
import com.tekfilo.jewellery.sku.service.SkuService;
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
@RequestMapping("/jew/sku")
public class SkuController {

    @Autowired
    SkuService skuService;

    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<SkuEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<SkuEntity>>(skuService.findAllSkus(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<SkuEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SkuEntity>(skuService.findSkuById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<JewResponse> save(@RequestBody SkuDto skuDto) {
        JewResponse response = new JewResponse();
        try {
            SkuEntity entity = skuService.save(skuDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<JewResponse> update(@RequestBody SkuDto skuDto) {
        JewResponse response = new JewResponse();
        try {
            SkuEntity entity = skuService.save(skuDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<JewResponse> remove(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            SkuEntity entity = skuService.findSkuById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            skuService.removeSku(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<JewResponse> remove(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<SkuEntity> entities = skuService.findAllSkuEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            skuService.removeAllSku(entities);
            response.setId(null);
            response.setStatus(100);
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
            List<SkuEntity> entities = skuService.findAllSkuEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            skuService.lockSku(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_LOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/unlock")
    public ResponseEntity<JewResponse> unlock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<SkuEntity> entities = skuService.findAllSkuEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            skuService.unlockSku(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/findcomponentbymainid/{id}")
    public ResponseEntity<List<SkuComponentEntity>> findSkuComponentById(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<SkuComponentEntity>>(this.skuService.findSkuComponentList(id), HttpStatus.OK);
    }

    @PostMapping("/savecomponent")
    public ResponseEntity<JewResponse> saveComponent(@RequestBody SkuComponentDto skuComponentDto) {
        JewResponse response = new JewResponse();
        try {
            SkuComponentEntity entity = skuService.saveSkuComponent(skuComponentDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifycomponent")
    public ResponseEntity<JewResponse> updatecomponent(@RequestBody SkuComponentDto skuComponentDto) {
        JewResponse response = new JewResponse();
        try {
            SkuComponentEntity entity = skuService.saveSkuComponent(skuComponentDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removecomponent/{id}")
    public ResponseEntity<JewResponse> removeComponent(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            SkuComponentEntity entity = skuService.findSkuComponentById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            skuService.deleteSkuComponent(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findlabourbymainid/{id}")
    public ResponseEntity<List<SkuLabourEntity>> findSkuLabourById(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<SkuLabourEntity>>(this.skuService.findSkuLabourList(id), HttpStatus.OK);
    }

    @PostMapping("/savelabour")
    public ResponseEntity<JewResponse> saveLabour(@RequestBody SkuLabourDto skuLabourDto) {
        JewResponse response = new JewResponse();
        try {
            SkuLabourEntity entity = skuService.saveSkuLabour(skuLabourDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifylabour")
    public ResponseEntity<JewResponse> updatelabour(@RequestBody SkuLabourDto skuLabourDto) {
        JewResponse response = new JewResponse();
        try {
            SkuLabourEntity entity = skuService.saveSkuLabour(skuLabourDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removelabour/{id}")
    public ResponseEntity<JewResponse> removeLabour(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            SkuLabourEntity entity = skuService.findSkuLabourById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            skuService.deleteSkuLabour(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findmouldpartbymainid/{id}")
    public ResponseEntity<List<SkuMouldPartEntity>> findSkuMouldPartById(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<SkuMouldPartEntity>>(this.skuService.findSkuMouldPartList(id), HttpStatus.OK);
    }

    @PostMapping("/savemouldpart")
    public ResponseEntity<JewResponse> saveMouldPart(@RequestBody SkuMouldPartDto skuMouldPartDto) {
        JewResponse response = new JewResponse();
        try {
            SkuMouldPartEntity entity = skuService.saveSkuMouldPart(skuMouldPartDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifymouldpart")
    public ResponseEntity<JewResponse> updatemouldpart(@RequestBody SkuMouldPartDto skuMouldPartDto) {
        JewResponse response = new JewResponse();
        try {
            SkuMouldPartEntity entity = skuService.saveSkuMouldPart(skuMouldPartDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removemouldpart/{id}")
    public ResponseEntity<JewResponse> removeMouldPart(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            SkuMouldPartEntity entity = skuService.findSkuMouldPartById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            skuService.deleteSkuMouldPart(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findfindingbymainid/{id}")
    public ResponseEntity<List<SkuFindingEntity>> findSkuFindingById(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<SkuFindingEntity>>(this.skuService.findSkuFindingList(id), HttpStatus.OK);
    }

    @PostMapping("/savefinding")
    public ResponseEntity<JewResponse> saveFinding(@RequestBody SkuFindingDto skuFindingDto) {
        JewResponse response = new JewResponse();
        try {
            SkuFindingEntity entity = skuService.saveSkuFinding(skuFindingDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifyfinding")
    public ResponseEntity<JewResponse> updatefinding(@RequestBody SkuFindingDto skuFindingDto) {
        JewResponse response = new JewResponse();
        try {
            SkuFindingEntity entity = skuService.saveSkuFinding(skuFindingDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removefinding/{id}")
    public ResponseEntity<JewResponse> removeFinding(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            SkuFindingEntity entity = skuService.findSkuFindingById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            skuService.deleteSkuFinding(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/findwaxbymainid/{id}")
    public ResponseEntity<List<SkuWaxEntity>> findSkuWaxById(@PathVariable("id") Integer id) {
        return new ResponseEntity<List<SkuWaxEntity>>(this.skuService.findSkuWaxList(id), HttpStatus.OK);
    }

    @PostMapping("/savewax")
    public ResponseEntity<JewResponse> saveWax(@RequestBody SkuWaxDto skuWaxDto) {
        JewResponse response = new JewResponse();
        try {
            SkuWaxEntity entity = skuService.saveSkuWax(skuWaxDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modifywax")
    public ResponseEntity<JewResponse> updatewax(@RequestBody SkuWaxDto skuWaxDto) {
        JewResponse response = new JewResponse();
        try {
            SkuWaxEntity entity = skuService.saveSkuWax(skuWaxDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removewax/{id}")
    public ResponseEntity<JewResponse> removeWax(@PathVariable("id") Integer id) {
        JewResponse response = new JewResponse();
        try {
            SkuWaxEntity entity = skuService.findSkuWaxById(id);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            entity.setIsDeleted(1);
            skuService.deleteSkuWax(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }
}
