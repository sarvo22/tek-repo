package com.tekfilo.jewellery.configmaster.controller;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.configmaster.dto.CollectionDto;
import com.tekfilo.jewellery.configmaster.dto.MarketDto;
import com.tekfilo.jewellery.configmaster.dto.ProductTypeDto;
import com.tekfilo.jewellery.configmaster.dto.SettingTypeDto;
import com.tekfilo.jewellery.configmaster.entity.CollectionEntity;
import com.tekfilo.jewellery.configmaster.entity.MarketEntity;
import com.tekfilo.jewellery.configmaster.entity.ProductTypeEntity;
import com.tekfilo.jewellery.configmaster.entity.SettingTypeEntity;
import com.tekfilo.jewellery.configmaster.service.impl.ConfigMasterService;
import com.tekfilo.jewellery.multitenancy.UserContext;
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
@RequestMapping("/jew/configmaster")
public class ConfigMasterController {

    @Autowired
    ConfigMasterService configMasterService;

    @PostMapping("/market/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<MarketEntity>> findAll(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<MarketEntity>>(configMasterService.findAllMarkets(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @PostMapping("/producttype/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<ProductTypeEntity>> findAllProductTypes(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<ProductTypeEntity>>(configMasterService.findAllProductType(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @PostMapping("/settingtype/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<SettingTypeEntity>> findAllSettingTypes(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<SettingTypeEntity>>(configMasterService.findAllSettingType(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @PostMapping("/collection/search/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<CollectionEntity>> findAllCollection(
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<CollectionEntity>>(configMasterService.findAllCollections(pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }

    @GetMapping("/market/findbyid/{id}")
    public ResponseEntity<MarketEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<MarketEntity>(configMasterService.findMarketById(id), HttpStatus.OK);
    }

    @GetMapping("/producttype/findbyid/{id}")
    public ResponseEntity<ProductTypeEntity> findProductTypeById(@PathVariable("id") Integer id) {
        return new ResponseEntity<ProductTypeEntity>(configMasterService.findProductTypeById(id), HttpStatus.OK);
    }

    @GetMapping("/settingtype/findbyid/{id}")
    public ResponseEntity<SettingTypeEntity> findSettingTypeById(@PathVariable("id") Integer id) {
        return new ResponseEntity<SettingTypeEntity>(configMasterService.findSettingTypeById(id), HttpStatus.OK);
    }

    @GetMapping("/collection/findbyid/{id}")
    public ResponseEntity<CollectionEntity> findCollectionById(@PathVariable("id") Integer id) {
        return new ResponseEntity<CollectionEntity>(configMasterService.findCollectionById(id), HttpStatus.OK);
    }

    @PostMapping("/market/save")
    public ResponseEntity<JewResponse> save(@RequestBody MarketDto marketDto) {
        JewResponse response = new JewResponse();
        try {
            MarketEntity entity = configMasterService.saveMarket(marketDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/producttype/save")
    public ResponseEntity<JewResponse> saveProductType(@RequestBody ProductTypeDto productTypeDto) {
        JewResponse response = new JewResponse();
        try {
            ProductTypeEntity entity = configMasterService.saveProductType(productTypeDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PostMapping("/settingtype/save")
    public ResponseEntity<JewResponse> saveSettingType(@RequestBody SettingTypeDto settingTypeDto) {
        JewResponse response = new JewResponse();
        try {
            SettingTypeEntity entity = configMasterService.saveSettingType(settingTypeDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/collection/save")
    public ResponseEntity<JewResponse> saveCollection(@RequestBody CollectionDto collectionDto) {
        JewResponse response = new JewResponse();
        try {
            CollectionEntity entity = configMasterService.saveCollection(collectionDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while saving Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/market/modify")
    public ResponseEntity<JewResponse> update(@RequestBody MarketDto marketDto) {
        JewResponse response = new JewResponse();
        try {
            MarketEntity entity = configMasterService.saveMarket(marketDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/producttype/modify")
    public ResponseEntity<JewResponse> updateProductType(@RequestBody ProductTypeDto productTypeDto) {
        JewResponse response = new JewResponse();
        try {
            ProductTypeEntity entity = configMasterService.saveProductType(productTypeDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/settingtype/modify")
    public ResponseEntity<JewResponse> updateSettingType(@RequestBody SettingTypeDto settingTypeDto) {
        JewResponse response = new JewResponse();
        try {
            SettingTypeEntity entity = configMasterService.saveSettingType(settingTypeDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/collection/modify")
    public ResponseEntity<JewResponse> updateCollection(@RequestBody CollectionDto collectionDto) {
        JewResponse response = new JewResponse();
        try {
            CollectionEntity entity = configMasterService.saveCollection(collectionDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/market/remove/{id}/{operateby}")
    public ResponseEntity<JewResponse> remove(@PathVariable("id") Integer id,
                                              @PathVariable("operateby") Integer operateBy) {
        JewResponse response = new JewResponse();
        try {
            MarketEntity entity = configMasterService.findMarketById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            configMasterService.removeMarket(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/producttype/remove/{id}/{operateby}")
    public ResponseEntity<JewResponse> removeProductType(@PathVariable("id") Integer id,
                                                         @PathVariable("operateby") Integer operateBy) {
        JewResponse response = new JewResponse();
        try {
            ProductTypeEntity entity = configMasterService.findProductTypeById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            configMasterService.removeProductType(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/settingtype/remove/{id}/{operateby}")
    public ResponseEntity<JewResponse> removeSettingType(@PathVariable("id") Integer id,
                                                         @PathVariable("operateby") Integer operateBy) {
        JewResponse response = new JewResponse();
        try {
            SettingTypeEntity entity = configMasterService.findSettingTypeById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            configMasterService.removeSettingType(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/collection/remove/{id}/{operateby}")
    public ResponseEntity<JewResponse> removeCollection(@PathVariable("id") Integer id,
                                                        @PathVariable("operateby") Integer operateBy) {
        JewResponse response = new JewResponse();
        try {
            CollectionEntity entity = configMasterService.findCollectionById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            configMasterService.removeCollection(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(exception.getCause().getMessage());
            log.error("Exception raised while modifying Customer  {}" + exception.getMessage());
        }
        return new ResponseEntity<JewResponse>(response, HttpStatus.OK);
    }


    @PutMapping("/market/removeall")
    public ResponseEntity<JewResponse> remove(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<MarketEntity> entities = configMasterService.findAllMarketEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            configMasterService.removeAllMarket(entities);
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

    @PutMapping("/producttype/removeall")
    public ResponseEntity<JewResponse> removeAllProductTypes(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<ProductTypeEntity> entities = configMasterService.findAllProductTypeEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            configMasterService.removeAllProductType(entities);
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

    @PutMapping("/settingtype/removeall")
    public ResponseEntity<JewResponse> removeAllSettingTypes(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<SettingTypeEntity> entities = configMasterService.findAllSettingTypeEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            configMasterService.removeAllSettingType(entities);
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

    @PutMapping("/collection/removeall")
    public ResponseEntity<JewResponse> removeCollection(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<CollectionEntity> entities = configMasterService.findAllCollectionEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            configMasterService.removeAllCollection(entities);
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

    @PutMapping("/market/lock")
    public ResponseEntity<JewResponse> lock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<MarketEntity> entities = configMasterService.findAllMarketEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            configMasterService.lockMarket(entities);
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

    @PutMapping("/producttype/lock")
    public ResponseEntity<JewResponse> lockProductType(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<ProductTypeEntity> entities = configMasterService.findAllProductTypeEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            configMasterService.lockProductType(entities);
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

    @PutMapping("/settingtype/lock")
    public ResponseEntity<JewResponse> lockSettingType(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<SettingTypeEntity> entities = configMasterService.findAllSettingTypeEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            configMasterService.lockSettingType(entities);
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


    @PutMapping("/collection/lock")
    public ResponseEntity<JewResponse> lockCollection(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<CollectionEntity> entities = configMasterService.findAllCollectionEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            configMasterService.lockCollection(entities);
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


    @PutMapping("/market/unlock")
    public ResponseEntity<JewResponse> unlock(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<MarketEntity> entities = configMasterService.findAllMarketEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            configMasterService.unlockMarket(entities);
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


    @PutMapping("/producttype/unlock")
    public ResponseEntity<JewResponse> unlockProductTypes(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<ProductTypeEntity> entities = configMasterService.findAllProductTypeEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            configMasterService.unlockProductType(entities);
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

    @PutMapping("/settingtype/unlock")
    public ResponseEntity<JewResponse> unlockSettingTypes(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<SettingTypeEntity> entities = configMasterService.findAllSettingTypeEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            configMasterService.unlockSettingType(entities);
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

    @PutMapping("/collection/unlock")
    public ResponseEntity<JewResponse> unlockCollection(@RequestBody List<Integer> ids) {
        JewResponse response = new JewResponse();
        try {
            List<CollectionEntity> entities = configMasterService.findAllCollectionEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            configMasterService.unlockCollection(entities);
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
}