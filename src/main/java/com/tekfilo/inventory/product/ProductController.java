package com.tekfilo.inventory.product;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.product.price.ProductPriceEntity;
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

@Slf4j
@RestController
@RequestMapping("/inventory/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/search/{category}/{pageno}/{pagesize}/{sortby}/{sortdirection}")
    public ResponseEntity<Page<ProductEntity>> findAll(
            @PathVariable("category") String prodCategory,
            @PathVariable("pageno") int pageNo,
            @PathVariable("pagesize") int pageSize,
            @PathVariable("sortby") String sortColumn,
            @PathVariable("sortdirection") String sortdirection,
            @RequestBody List<FilterClause> filterClause) {
        return new ResponseEntity<Page<ProductEntity>>(productService.findAll(prodCategory, pageNo, pageSize, sortColumn, sortdirection == null ? "asc" : sortdirection, filterClause), HttpStatus.OK);
    }


    @GetMapping("/findbyid/{id}")
    public ResponseEntity<ProductEntity> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity<ProductEntity>(productService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<InventoryResponse> save(@RequestBody ProductDto productDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            int count = productService.validateProductNo(productDto.getProductNo());
            if (count > 0) {
                response.setStatus(MessageConstants.ERROR_STATUS);
                response.setLangStatus("error_101");
                response.setMessage(MessageConstants.LOT_EXITS);
                return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
            }
            ProductEntity entity = productService.save(productDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("save_100");
            response.setMessage(MessageConstants.RECORD_SAVE);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<InventoryResponse> update(@RequestBody ProductDto productDto) {
        InventoryResponse response = new InventoryResponse();
        try {
            ProductEntity entity = productService.save(productDto);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_UPDATED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/remove/{id}/{operateby}")
    public ResponseEntity<InventoryResponse> remove(@PathVariable("id") Integer id,
                                                    @PathVariable("operateby") Integer operateBy) {
        InventoryResponse response = new InventoryResponse();
        try {
            ProductEntity entity = productService.findById(id);
            entity.setModifiedBy(operateBy);
            entity.setIsDeleted(1);
            productService.remove(entity);
            response.setId(entity.getId());
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage(MessageConstants.RECORD_DELETED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/removeall")
    public ResponseEntity<InventoryResponse> remove(@RequestBody List<Integer> ids) {
        InventoryResponse response = new InventoryResponse();
        try {
            List<ProductEntity> entities = productService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsDeleted(1);
            });
            productService.removeAll(entities);
            response.setId(null);
            response.setStatus(100);
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
            List<ProductEntity> entities = productService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(1);
            });
            productService.lock(entities);
            response.setId(null);
            response.setStatus(100);
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
            List<ProductEntity> entities = productService.findAllEntitiesByIds(ids);
            entities.stream().forEach(entity -> {
                entity.setModifiedBy(UserContext.getLoggedInUser());
                entity.setIsLocked(0);
            });
            productService.unlock(entities);
            response.setId(null);
            response.setStatus(100);
            response.setLangStatus("lock_100");
            response.setMessage(MessageConstants.RECORD_UNLOCKED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            TekfiloUtils.setErrorResponse(response,exception);
        }
        return new ResponseEntity<InventoryResponse>(response, HttpStatus.OK);
    }


    @GetMapping("/pricehistory/{productid}")
    public ResponseEntity<List<ProductPriceEntity>> priceHistory(@PathVariable("productid") Integer productId) {
        List<ProductPriceEntity> priceHistoryList = productService.findProductPriceByProduct(productId);
        return new ResponseEntity<List<ProductPriceEntity>>(priceHistoryList, HttpStatus.OK);
    }

    @GetMapping("/searchproductbykey/{searchkey}")
    public ResponseEntity<List<ProductEntity>> getProductByKey(@PathVariable("searchkey") String searchKey) {
        return new ResponseEntity<List<ProductEntity>>(this.productService.getProductList(searchKey), HttpStatus.OK);
    }

}
