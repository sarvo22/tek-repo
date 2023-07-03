package com.tekfilo.inventory.shape;

import com.tekfilo.inventory.commodity.CommodityEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShapeRepository extends JpaRepository<ShapeEntity, Integer>, JpaSpecificationExecutor {

    @Query("select a from ShapeEntity a ")
    List<ShapeEntity> findByName(Specification specification);

    @Query("select sh from ShapeEntity sh where sh.isDeleted = 0 and sh.companyId = :currentCompany and sh.group.groupType = :category")
    List<ShapeEntity> findShapeList(String category, Integer currentCompany);

    @Query("from ShapeEntity where lower(shapeName) = :name")
    List<ShapeEntity> findAllByName(String name);
}
