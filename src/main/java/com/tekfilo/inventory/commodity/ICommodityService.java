package com.tekfilo.inventory.commodity;

import com.tekfilo.inventory.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICommodityService {
    Page<CommodityEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    CommodityEntity save(CommodityDto commodityDto) throws Exception;

    void modify(CommodityDto commodityDto) throws Exception;

    CommodityEntity findById(Integer id);

    void remove(CommodityEntity entity);


    List<CommodityEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<CommodityEntity> entities) throws Exception;

    void lock(List<CommodityEntity> entities) throws Exception;

    void unlock(List<CommodityEntity> entities) throws Exception;

    int checkNameExists(String commodityName);

    boolean isCommodityExists(String commodityName);

    List<CommodityEntity> findListByName(String commodityName);
}
