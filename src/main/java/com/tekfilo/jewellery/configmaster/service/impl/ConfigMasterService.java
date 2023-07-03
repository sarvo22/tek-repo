package com.tekfilo.jewellery.configmaster.service.impl;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.configmaster.dto.CollectionDto;
import com.tekfilo.jewellery.configmaster.dto.MarketDto;
import com.tekfilo.jewellery.configmaster.dto.ProductTypeDto;
import com.tekfilo.jewellery.configmaster.dto.SettingTypeDto;
import com.tekfilo.jewellery.configmaster.entity.CollectionEntity;
import com.tekfilo.jewellery.configmaster.entity.MarketEntity;
import com.tekfilo.jewellery.configmaster.entity.ProductTypeEntity;
import com.tekfilo.jewellery.configmaster.entity.SettingTypeEntity;
import com.tekfilo.jewellery.configmaster.repository.CollectionRepository;
import com.tekfilo.jewellery.configmaster.repository.MarketRepository;
import com.tekfilo.jewellery.configmaster.repository.ProductTypeRepository;
import com.tekfilo.jewellery.configmaster.repository.SettingTypeRepository;
import com.tekfilo.jewellery.configmaster.service.IConfigMasterService;
import com.tekfilo.jewellery.multitenancy.CompanyContext;
import com.tekfilo.jewellery.multitenancy.UserContext;
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
public class ConfigMasterService implements IConfigMasterService {

    @Autowired
    MarketRepository marketRepository;

    @Autowired
    ProductTypeRepository productTypeRepository;

    @Autowired
    CollectionRepository  collectionRepository;

    @Autowired
    SettingTypeRepository settingTypeRepository;

    @Override
    public Page<MarketEntity> findAllMarkets(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.marketRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public MarketEntity saveMarket(MarketDto marketDto) throws Exception {
        return marketRepository.save(convertToMarketEntity(marketDto));
    }

    private MarketEntity convertToMarketEntity(MarketDto marketDto) {
        MarketEntity entity = new MarketEntity();
        BeanUtils.copyProperties(marketDto, entity);
        entity.setSequence(marketDto.getSequence() == null ? 0 : marketDto.getSequence());
        entity.setIsLocked(marketDto.getIsLocked() == null ? 0 : marketDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(marketDto.getIsDeleted() == null ? 0 : marketDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyMarket(MarketDto marketDto) throws Exception {
        marketRepository.save(convertToMarketEntity(marketDto));
    }

    @Override
    public MarketEntity findMarketById(Integer id) {
        return marketRepository.findById(id).orElse(null);
    }

    @Override
    public void removeMarket(MarketEntity entity) throws Exception {
        marketRepository.save(entity);
    }


    @Override
    public List<MarketEntity> findAllMarketEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.marketRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAllMarket(List<MarketEntity> entities) throws Exception {
        this.marketRepository.saveAll(entities);
    }

    @Override
    public void lockMarket(List<MarketEntity> entities) throws Exception {
        this.marketRepository.saveAll(entities);
    }

    @Override
    public void unlockMarket(List<MarketEntity> entities) throws Exception {
        this.marketRepository.saveAll(entities);
    }




    @Override
    public Page<ProductTypeEntity> findAllProductType(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.productTypeRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public ProductTypeEntity saveProductType(ProductTypeDto productTypeDto) throws Exception {
        return productTypeRepository.save(convertToProductTypeEntity(productTypeDto));
    }

    private ProductTypeEntity convertToProductTypeEntity(ProductTypeDto productTypeDto) {
        ProductTypeEntity entity = new ProductTypeEntity();
        BeanUtils.copyProperties(productTypeDto, entity);
        entity.setSequence(productTypeDto.getSequence() == null ? 0 : productTypeDto.getSequence());
        entity.setIsLocked(productTypeDto.getIsLocked() == null ? 0 : productTypeDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(productTypeDto.getIsDeleted() == null ? 0 : productTypeDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyProductType(ProductTypeDto productTypeDto) throws Exception {
        productTypeRepository.save(convertToProductTypeEntity(productTypeDto));
    }

    @Override
    public ProductTypeEntity findProductTypeById(Integer id) {
        return productTypeRepository.findById(id).orElse(null);
    }

    @Override
    public void removeProductType(ProductTypeEntity entity) throws Exception {
        productTypeRepository.save(entity);
    }


    @Override
    public List<ProductTypeEntity> findAllProductTypeEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.productTypeRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAllProductType(List<ProductTypeEntity> entities) throws Exception {
        this.productTypeRepository.saveAll(entities);
    }

    @Override
    public void lockProductType(List<ProductTypeEntity> entities) throws Exception {
        this.productTypeRepository.saveAll(entities);
    }

    @Override
    public void unlockProductType(List<ProductTypeEntity> entities) throws Exception {
        this.productTypeRepository.saveAll(entities);
    }



    @Override
    public Page<CollectionEntity> findAllCollections(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.collectionRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public CollectionEntity saveCollection(CollectionDto collectionDto) throws Exception {
        return collectionRepository.save(convertToCollectionEntity(collectionDto));
    }

    private CollectionEntity convertToCollectionEntity(CollectionDto collectionDto) {
        CollectionEntity entity = new CollectionEntity();
        BeanUtils.copyProperties(collectionDto, entity);
        entity.setSequence(collectionDto.getSequence() == null ? 0 : collectionDto.getSequence());
        entity.setIsLocked(collectionDto.getIsLocked() == null ? 0 : collectionDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(collectionDto.getIsDeleted() == null ? 0 : collectionDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifyCollection(CollectionDto collectionDto) throws Exception {
        collectionRepository.save(convertToCollectionEntity(collectionDto));
    }

    @Override
    public CollectionEntity findCollectionById(Integer id) {
        return collectionRepository.findById(id).orElse(null);
    }

    @Override
    public void removeCollection(CollectionEntity entity) throws Exception {
        collectionRepository.save(entity);
    }


    @Override
    public List<CollectionEntity> findAllCollectionEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.collectionRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAllCollection(List<CollectionEntity> entities) throws Exception {
        this.collectionRepository.saveAll(entities);
    }

    @Override
    public void lockCollection(List<CollectionEntity> entities) throws Exception {
        this.collectionRepository.saveAll(entities);
    }

    @Override
    public void unlockCollection(List<CollectionEntity> entities) throws Exception {
        this.collectionRepository.saveAll(entities);
    }



    @Override
    public Page<SettingTypeEntity> findAllSettingType(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.settingTypeRepository.findAll(filterClauseAppender.getFilterClause(filterClause), pageable);
    }

    @Override
    public SettingTypeEntity saveSettingType(SettingTypeDto settingTypeDto) throws Exception {
        return settingTypeRepository.save(convertToSettingTypeEntity(settingTypeDto));
    }

    private SettingTypeEntity convertToSettingTypeEntity(SettingTypeDto settingTypeDto) {
        SettingTypeEntity entity = new SettingTypeEntity();
        BeanUtils.copyProperties(settingTypeDto, entity);
        entity.setSequence(settingTypeDto.getSequence() == null ? 0 : settingTypeDto.getSequence());
        entity.setIsLocked(settingTypeDto.getIsLocked() == null ? 0 : settingTypeDto.getIsLocked());
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(settingTypeDto.getIsDeleted() == null ? 0 : settingTypeDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modifySettingType(SettingTypeDto settingTypeDto) throws Exception {
        settingTypeRepository.save(convertToSettingTypeEntity(settingTypeDto));
    }

    @Override
    public SettingTypeEntity findSettingTypeById(Integer id) {
        return settingTypeRepository.findById(id).orElse(null);
    }

    @Override
    public void removeSettingType(SettingTypeEntity entity) throws Exception {
        settingTypeRepository.save(entity);
    }


    @Override
    public List<SettingTypeEntity> findAllSettingTypeEntitiesByIds(List<Integer> ids) {
        FilterClauseAppender filterClauseAppender = new FilterClauseAppender();
        return this.settingTypeRepository.findAll(filterClauseAppender.getInClassFilter(ids));
    }

    @Override
    public void removeAllSettingType(List<SettingTypeEntity> entities) throws Exception {
        this.settingTypeRepository.saveAll(entities);
    }

    @Override
    public void lockSettingType(List<SettingTypeEntity> entities) throws Exception {
        this.settingTypeRepository.saveAll(entities);
    }

    @Override
    public void unlockSettingType(List<SettingTypeEntity> entities) throws Exception {
        this.settingTypeRepository.saveAll(entities);
    }
}
