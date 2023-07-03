package com.tekfilo.inventory.product;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.multitenancy.CompanyContext;
import com.tekfilo.inventory.multitenancy.UserContext;
import com.tekfilo.inventory.product.price.ProductPriceEntity;
import com.tekfilo.inventory.product.price.ProductPriceRepository;
import com.tekfilo.inventory.util.FilterClauseAppender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService implements IProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Override
    public Page<ProductEntity> findAll(String prodCategory, int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        FilterClause prodFilterClause = new FilterClause();
        prodFilterClause.setKey("prodCategory");
        prodFilterClause.setValue(prodCategory);
        filterClause.add(prodFilterClause);
        return this.productRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public ProductEntity save(ProductDto productDto) throws DataIntegrityViolationException {
        return productRepository.save(convertToEntity(productDto));
    }

    private ProductEntity convertToEntity(ProductDto productDto) {
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(productDto, entity);
        entity.setSequence(productDto.getSequence() == null ? 0 : productDto.getSequence());
        entity.setIsLocked(productDto.getIsLocked() == null ? 0 : productDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(productDto.getIsDeleted() == null ? 0 : productDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(ProductDto productDto) throws DataIntegrityViolationException {
        productRepository.save(convertToEntity(productDto));
    }


    @Override
    public ProductEntity findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(ProductEntity entity) {
        productRepository.save(entity);
    }


    public List<ProductEntity> findAllEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.productRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    public void removeAll(List<ProductEntity> entities) throws DataIntegrityViolationException {
        this.productRepository.saveAll(entities);
    }

    public void lock(List<ProductEntity> entities) throws DataIntegrityViolationException {
        this.productRepository.saveAll(entities);
    }

    public void unlock(List<ProductEntity> entities) throws DataIntegrityViolationException {
        this.productRepository.saveAll(entities);
    }

    @Override
    public List<ProductPriceEntity> findProductPriceByProduct(Integer productId) {

        return productPriceRepository.findProductPriceByProduct(productId);
    }

    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    @Override
    public int validateProductNo(String productNo) {
        return this.productRepository.checkLotCodeExists(productNo.toLowerCase().trim());
    }


}
