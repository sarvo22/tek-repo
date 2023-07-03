package com.tekfilo.admin.factorychart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryChartRepository extends JpaRepository<FactoryChartEntity, Integer>, JpaSpecificationExecutor {
}
