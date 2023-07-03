package com.tekfilo.jewellery.product.service.impl;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
import com.tekfilo.jewellery.product.dto.ProductComponentDto;
import com.tekfilo.jewellery.product.dto.ProductDto;
import com.tekfilo.jewellery.product.dto.ProductLabourDto;
import com.tekfilo.jewellery.product.entity.ProductComponentEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import com.tekfilo.jewellery.product.entity.ProductLabourEntity;
import com.tekfilo.jewellery.product.repository.ProductComponentRepository;
import com.tekfilo.jewellery.product.repository.ProductLabourRepository;
import com.tekfilo.jewellery.product.repository.ProductRepository;
import com.tekfilo.jewellery.product.service.IProductService;
import com.tekfilo.jewellery.util.FilterClauseAppender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    ProductComponentRepository productComponentRepository;

    @Autowired
    ProductLabourRepository productLabourRepository;

    @Override
    public Page<ProductEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.productRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public ProductEntity save(ProductDto productDto) throws Exception {
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
    public void modify(ProductDto productDto) throws Exception {
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

    public void removeAll(List<ProductEntity> entities) throws Exception {
        this.productRepository.saveAll(entities);
    }

    public void lock(List<ProductEntity> entities) throws Exception {
        this.productRepository.saveAll(entities);
    }

    public void unlock(List<ProductEntity> entities) throws Exception {
        this.productRepository.saveAll(entities);
    }


    @Override
    public List<ProductEntity> getProductList(String searchKey) {
        List<ProductEntity> productEntityList = this.productRepository.findByProductName(
                (searchKey == null ? "%%" : searchKey.toLowerCase()), CompanyContext.getCurrentCompany());
        return (productEntityList);
    }

    @Override
    public ProductComponentEntity saveComponent(ProductComponentDto productComponentDto) throws Exception {
        return this.productComponentRepository.save(convertToComponentEntity(productComponentDto));
    }

    @Override
    public void modifyComponent(ProductComponentDto productComponentDto) throws Exception {
        this.productComponentRepository.save(convertToComponentEntity(productComponentDto));
    }

    @Override
    public void removeComponent(ProductComponentEntity entity) throws Exception {
        this.productComponentRepository.save(entity);
    }

    private ProductComponentEntity convertToComponentEntity(ProductComponentDto productComponentDto) {
        ProductComponentEntity entity = new ProductComponentEntity();
        BeanUtils.copyProperties(productComponentDto, entity);
        entity.setConsumedQty1(productComponentDto.getConsumedQty1() == null ? 0 : productComponentDto.getConsumedQty1());
        entity.setConsumedQty2(productComponentDto.getConsumedQty2() == null ? 0 : productComponentDto.getConsumedQty2());
        entity.setBrokenQty1(productComponentDto.getBrokenQty1() == null ? 0 : productComponentDto.getBrokenQty1());
        entity.setBrokenQty2(productComponentDto.getBrokenQty2() == null ? 0 : productComponentDto.getBrokenQty2());
        entity.setLossQty1(productComponentDto.getLossQty1() == null ? 0 : productComponentDto.getLossQty1());
        entity.setLossQty2(productComponentDto.getLossQty2() == null ? 0 : productComponentDto.getLossQty2());
        entity.setOriginalBrokenQty1(productComponentDto.getOriginalBrokenQty1() == null ? 0 : productComponentDto.getOriginalBrokenQty1());
        entity.setOriginalBrokenQty2(productComponentDto.getOriginalBrokenQty2() == null ? 0 : productComponentDto.getOriginalBrokenQty2());
        entity.setOriginalLossQty1(productComponentDto.getOriginalLossQty1() == null ? 0 : productComponentDto.getOriginalLossQty1());
        entity.setOriginalLossQty2(productComponentDto.getOriginalLossQty2() == null ? 0 : productComponentDto.getOriginalLossQty2());
        entity.setTotalQty1(productComponentDto.getTotalQty1() == null ? 0 : productComponentDto.getTotalQty1());
        entity.setTotalQty1(productComponentDto.getTotalQty2() == null ? 0 : productComponentDto.getTotalQty2());
        entity.setIsCenterStone(productComponentDto.getIsCenterStone() == null ? 0 : productComponentDto.getIsCenterStone());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(0);
        return entity;
    }

    @Override
    public ProductComponentEntity findComponentById(Integer id) {
        return this.productComponentRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductComponentEntity> findComponentByJewId(Integer id) {
        return this.productComponentRepository.findAllByJewId(id);
    }


    @Override
    public List<ProductLabourEntity> findProductLabourList(Integer jewId) {
        return this.productLabourRepository.findAllByDetailId(jewId);
    }

    @Override
    public ProductLabourEntity saveProductLabour(ProductLabourDto productLabourDto) throws Exception {
        return this.productLabourRepository.save(convertToProductLabourEntity(productLabourDto));
    }

    private ProductLabourEntity convertToProductLabourEntity(ProductLabourDto productLabourDto) {
        ProductLabourEntity productLabourEntity = new ProductLabourEntity();
        BeanUtils.copyProperties(productLabourDto, productLabourEntity);
        productLabourEntity.setSequence(0);
        productLabourEntity.setIsLocked(0);
        productLabourEntity.setCreatedBy(UserContext.getLoggedInUser());
        productLabourEntity.setModifiedBy(UserContext.getLoggedInUser());
        productLabourEntity.setIsDeleted(0);
        return productLabourEntity;
    }

    @Override
    public void deleteProductLabour(ProductLabourEntity entity) throws Exception {
        this.productLabourRepository.save(entity);
    }

    @Override
    public ProductLabourEntity findProductLabourById(Integer id) {
        return this.productLabourRepository.findById(id).orElseThrow(null);
    }
}
