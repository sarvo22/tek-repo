package com.tekfilo.jewellery.product.repository;

import com.tekfilo.jewellery.product.entity.ProductLabourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductLabourRepository extends JpaRepository<ProductLabourEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM ProductLabourEntity d where d.isDeleted = 0 and d.jewId = :jewId")
    List<ProductLabourEntity> findAllByDetailId(Integer jewId);

}
