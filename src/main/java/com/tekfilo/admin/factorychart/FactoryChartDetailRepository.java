package com.tekfilo.admin.factorychart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactoryChartDetailRepository extends JpaRepository<FactoryChartDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM FactoryChartDetailEntity c where c.isDeleted = 0 and factoryChartId = :id")
    List<FactoryChartDetailEntity> findDetailByMainId(Integer id);
}
