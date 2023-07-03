package com.tekfilo.inventory.commodity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityRepository extends PagingAndSortingRepository<CommodityEntity, Integer>, JpaSpecificationExecutor {

    @Query("select count(1) from CommodityEntity where lower(name) = :name")
    int checkNameExists(String name);

    @Query("from CommodityEntity where lower(name) = :name")
    List<CommodityEntity> findAllByCommodityName(String name);


}
