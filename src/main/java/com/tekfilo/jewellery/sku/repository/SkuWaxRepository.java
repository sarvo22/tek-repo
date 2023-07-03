package com.tekfilo.jewellery.sku.repository;

import com.tekfilo.jewellery.sku.entity.SkuMouldPartEntity;
import com.tekfilo.jewellery.sku.entity.SkuWaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuWaxRepository extends JpaRepository<SkuWaxEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM SkuWaxEntity d where d.isDeleted = 0 and d.skuId = :skuId")
    List<SkuWaxEntity> findAllByMainId(Integer skuId);

}
