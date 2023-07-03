package com.tekfilo.admin.customerchart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerChartDetailRepository extends JpaRepository<CustomerChartDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM CustomerChartDetailEntity c where c.isDeleted = 0 and customerChartId = :id")
    List<CustomerChartDetailEntity> findDetailByMainId(Integer id);
}
