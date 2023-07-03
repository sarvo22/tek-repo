package com.tekfilo.jewellery.sku.repository;

import com.tekfilo.jewellery.sku.entity.SkuComponentEntity;
import com.tekfilo.jewellery.sku.entity.SkuEntity;
import com.tekfilo.jewellery.sku.entity.SkuLabourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuLabourRepository extends JpaRepository<SkuLabourEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM SkuLabourEntity d where d.isDeleted = 0 and d.skuId = :skuId")
    List<SkuLabourEntity> findAllByMainId(Integer skuId);
}
