package com.tekfilo.account.costcenter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenterEntity, Integer> , JpaSpecificationExecutor {

    @Query("select d from CostCenterEntity d where d.isDeleted = 0 ")
    Page<CostCenterEntity> findAllCostCenters(Pageable pageable);
}
