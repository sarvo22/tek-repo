package com.tekfilo.jewellery.sku.repository;

import com.tekfilo.jewellery.sku.entity.SkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SkuRepository extends JpaRepository<SkuEntity, Integer>, JpaSpecificationExecutor {

    @Modifying
    @Transactional
    @Query("update SkuEntity m set m.imageUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

    @Modifying
    @Transactional
    @Query("update SkuEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDate=:modifiedDt where isDeleted = 0 and id in (:skuIds)")
    void deleteAllByIds(List<Integer> skuIds, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM SkuEntity as e WHERE e.skuNo IN (:names) and companyId = :companyId")
    List<SkuEntity> findBySkunos(@Param("names") List<String> names, Integer companyId);
}
