package com.tekfilo.jewellery.sku.repository;

import com.tekfilo.jewellery.sku.entity.SkuFindingEntity;
import com.tekfilo.jewellery.sku.entity.SkuMouldPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuMouldPartRepository extends JpaRepository<SkuMouldPartEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM SkuMouldPartEntity d where d.isDeleted = 0 and d.skuId = :skuId")
    List<SkuMouldPartEntity> findAllByMainId(Integer skuId);
}
