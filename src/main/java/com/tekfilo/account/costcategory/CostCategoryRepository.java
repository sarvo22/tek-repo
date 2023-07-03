package com.tekfilo.account.costcategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CostCategoryRepository extends JpaRepository<CostCategoryEntity, Integer>, JpaSpecificationExecutor {

    @Query("select d from CostCategoryEntity d where d.isDeleted = 0 ")
    Page<CostCategoryEntity> findAllCostCategorys(Pageable pageable);
}
