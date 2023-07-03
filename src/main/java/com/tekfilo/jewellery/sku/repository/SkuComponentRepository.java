package com.tekfilo.jewellery.sku.repository;

import com.tekfilo.jewellery.sku.entity.SkuComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SkuComponentRepository extends JpaRepository<SkuComponentEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM SkuComponentEntity d where d.isDeleted = 0 and d.skuId = :skuId")
    List<SkuComponentEntity> findAllByMainId(Integer skuId);

    @Modifying
    @Transactional
    @Query("update SkuComponentEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDate=:modifiedDt where isDeleted = 0 and skuId in (:skuIds)")
    void deleteAllBySkuIds(List<Integer> skuIds, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
