package com.tekfilo.jewellery.product.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.product.dto.ProductComponentDto;
import com.tekfilo.jewellery.product.dto.ProductDto;
import com.tekfilo.jewellery.product.dto.ProductLabourDto;
import com.tekfilo.jewellery.product.entity.ProductComponentEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import com.tekfilo.jewellery.product.entity.ProductLabourEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {

    Page<ProductEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    ProductEntity save(ProductDto productDto) throws Exception;

    void modify(ProductDto productDto) throws Exception;

    ProductEntity findById(Integer id);

    void remove(ProductEntity entity);


    List<ProductEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<ProductEntity> entities) throws Exception;

    void lock(List<ProductEntity> entities) throws Exception;


    void unlock(List<ProductEntity> entities) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    ProductComponentEntity saveComponent(ProductComponentDto productComponentDto) throws Exception;

    void modifyComponent(ProductComponentDto productComponentDto) throws Exception;

    void removeComponent(ProductComponentEntity entity) throws Exception;

    ProductComponentEntity findComponentById(Integer id);

    List<ProductComponentEntity> findComponentByJewId(Integer id);

    ProductLabourEntity findProductLabourById(Integer id);
    void deleteProductLabour(ProductLabourEntity entity) throws Exception;

    ProductLabourEntity saveProductLabour(ProductLabourDto productLabourDto) throws Exception;

    List<ProductLabourEntity> findProductLabourList(Integer invoiceDetailId);
}
