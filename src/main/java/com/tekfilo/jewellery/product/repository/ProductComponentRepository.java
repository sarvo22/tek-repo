package com.tekfilo.jewellery.product.repository;

import com.tekfilo.jewellery.product.entity.ProductComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductComponentRepository extends JpaRepository<ProductComponentEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM ProductComponentEntity j where j.isDeleted = 0 and j.jewId = :jewId")
    List<ProductComponentEntity> findAllByJewId(Integer jewId);
}
