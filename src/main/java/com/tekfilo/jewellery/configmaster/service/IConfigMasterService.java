package com.tekfilo.jewellery.configmaster.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.configmaster.dto.CollectionDto;
import com.tekfilo.jewellery.configmaster.dto.MarketDto;
import com.tekfilo.jewellery.configmaster.dto.ProductTypeDto;
import com.tekfilo.jewellery.configmaster.dto.SettingTypeDto;
import com.tekfilo.jewellery.configmaster.entity.CollectionEntity;
import com.tekfilo.jewellery.configmaster.entity.MarketEntity;
import com.tekfilo.jewellery.configmaster.entity.ProductTypeEntity;
import com.tekfilo.jewellery.configmaster.entity.SettingTypeEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IConfigMasterService {

    Page<MarketEntity> findAllMarkets(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    MarketEntity saveMarket(MarketDto marketDto) throws Exception;

    void modifyMarket(MarketDto marketDto) throws Exception;

    MarketEntity findMarketById(Integer id);

    void removeMarket(MarketEntity entity) throws Exception;

    List<MarketEntity> findAllMarketEntitiesByIds(List<Integer> ids);

    void removeAllMarket(List<MarketEntity> entities) throws Exception;

    void lockMarket(List<MarketEntity> entities) throws Exception;

    void unlockMarket(List<MarketEntity> entities) throws Exception;


    Page<ProductTypeEntity> findAllProductType(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    ProductTypeEntity saveProductType(ProductTypeDto productTypeDto) throws Exception;

    void modifyProductType(ProductTypeDto productTypeDto) throws Exception;

    ProductTypeEntity findProductTypeById(Integer id);

    void removeProductType(ProductTypeEntity entity) throws Exception;

    List<ProductTypeEntity> findAllProductTypeEntitiesByIds(List<Integer> ids);

    void removeAllProductType(List<ProductTypeEntity> entities) throws Exception;

    void lockProductType(List<ProductTypeEntity> entities) throws Exception;

    void unlockProductType(List<ProductTypeEntity> entities) throws Exception;


    Page<CollectionEntity> findAllCollections(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    CollectionEntity saveCollection(CollectionDto collectionDto) throws Exception;

    void modifyCollection(CollectionDto collectionDto) throws Exception;

    CollectionEntity findCollectionById(Integer id);

    void removeCollection(CollectionEntity entity) throws Exception;

    List<CollectionEntity> findAllCollectionEntitiesByIds(List<Integer> ids);

    void removeAllCollection(List<CollectionEntity> entities) throws Exception;

    void lockCollection(List<CollectionEntity> entities) throws Exception;

    void unlockCollection(List<CollectionEntity> entities) throws Exception;



    Page<SettingTypeEntity> findAllSettingType(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    SettingTypeEntity saveSettingType(SettingTypeDto settingTypeDto) throws Exception;

    void modifySettingType(SettingTypeDto settingTypeDto) throws Exception;

    SettingTypeEntity findSettingTypeById(Integer id);

    void removeSettingType(SettingTypeEntity entity) throws Exception;

    List<SettingTypeEntity> findAllSettingTypeEntitiesByIds(List<Integer> ids);

    void removeAllSettingType(List<SettingTypeEntity> entities) throws Exception;

    void lockSettingType(List<SettingTypeEntity> entities) throws Exception;

    void unlockSettingType(List<SettingTypeEntity> entities) throws Exception;
}
