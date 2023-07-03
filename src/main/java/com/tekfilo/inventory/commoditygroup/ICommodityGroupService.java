package com.tekfilo.inventory.commoditygroup;

import com.tekfilo.inventory.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICommodityGroupService {
    Page<CommodityGroupEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    CommodityGroupEntity save(CommodityGroupDto commodityGroupDto) throws Exception;

    void modify(CommodityGroupDto commodityGroupDto) throws Exception;

    CommodityGroupEntity findById(Integer id);

    void remove(CommodityGroupEntity entity);

    List<CommodityGroupEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<CommodityGroupEntity> entities) throws Exception;

    void lock(List<CommodityGroupEntity> entities) throws Exception;

    void unlock(List<CommodityGroupEntity> entities) throws Exception;

    int checkNameExists(String commodityGroupName);
}
