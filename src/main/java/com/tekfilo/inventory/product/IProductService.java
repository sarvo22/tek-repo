package com.tekfilo.inventory.product;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.product.price.ProductPriceEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {

    Page<ProductEntity> findAll(String prodCategory, int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    ProductEntity save(ProductDto productDto) throws DataIntegrityViolationException;

    void modify(ProductDto productDto) throws DataIntegrityViolationException;

    ProductEntity findById(Integer id);

    void remove(ProductEntity entity);


    List<ProductEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<ProductEntity> entities) throws DataIntegrityViolationException;

    void lock(List<ProductEntity> entities) throws DataIntegrityViolationException;


    void unlock(List<ProductEntity> entities) throws DataIntegrityViolationException;


    List<ProductPriceEntity> findProductPriceByProduct(Integer productId);

    List<ProductEntity> getProductList(String searchKey);

    int validateProductNo(String productNo);
}
