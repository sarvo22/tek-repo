package com.tekfilo.inventory.color;

import com.tekfilo.inventory.base.FilterClause;
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

@Slf4j
@RestController
@RequestMapping("/inventory/color")
public class ColorController {

    @Autowired
    ColorService colorService;


    @PostMapping("/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<ColorEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<ColorEntity>>(colorService.findAll(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> save(@RequestBody ColorDto colorDto) {
        InventoryResponse response = new InventoryResponse();
        try {

            int count = this.colorService.checkNameExists(colorDto.getColorName());
            if (count > 0) {
                response.setStatus(MessageConstants.ERROR_STATUS);
                response.setLangStatus("error_101");
                response.setMessage(MessageConstants.COLOR_EXITS);
                return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
            }

            ColorEntity entity = colorService.save(colorDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("save_101");
            response.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
            log.error(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<InventoryResponse> modify(@RequestBody ColorDto colorDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            ColorEntity entity = colorService.save(colorDto);
            response.setId(entity.getId());
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("save_101");
            response.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
            log.error(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<InventoryResponse> delete(@PathVariable("id") Integer colorId) {
        InventoryResponse response = new InventoryResponse();
        try {
            ColorEntity entity = this.colorService.findById(colorId);
            entity.setIsDeleted(1);
            entity.setModifiedBy(UserContext.getLoggedInUser());
            colorService.remove(entity);
            response.setStatus(MessageConstants.STATUS_SUCCESS);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("save_101");
            response.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
            log.error(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> remove(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<ColorEntity> entities = colorService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            colorService.removeAll(entities);
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
            List<ColorEntity> entities = colorService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            colorService.lock(entities);
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
            List<ColorEntity> entities = colorService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            colorService.unlock(entities);
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


    @GetMapping("/colorlist/{category}")
    public ResponseEntity<List<ColorEntity>> colorList(@PathVariable("category") String category) {
        return new ResponseEntity<List<ColorEntity>>(this.colorService.getColorList(category), HttpStatus.OK);
    }
}
