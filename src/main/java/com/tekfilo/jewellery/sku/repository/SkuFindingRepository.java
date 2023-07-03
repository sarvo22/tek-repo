package com.tekfilo.jewellery.sku.repository;

import com.tekfilo.jewellery.sku.entity.SkuFindingEntity;
import com.tekfilo.jewellery.sku.entity.SkuLabourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuFindingRepository extends JpaRepository<SkuFindingEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM SkuFindingEntity d where d.isDeleted = 0 and d.skuId = :skuId")
    List<SkuFindingEntity> findAllByMainId(Integer skuId);
}
