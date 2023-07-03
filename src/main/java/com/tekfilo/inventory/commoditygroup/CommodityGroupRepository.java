package com.tekfilo.inventory.commoditygroup;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityGroupRepository extends PagingAndSortingRepository<CommodityGroupEntity, Integer>,
        JpaSpecificationExecutor {

    @Query("select count(1) from CommodityGroupEntity where lower(groupName) = :groupName")
    int checkNameExists(String groupName);
}
