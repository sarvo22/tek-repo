package com.tekfilo.admin.customerchart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerChartRepository extends JpaRepository<CustomerChartEntity, Integer>, JpaSpecificationExecutor {
}
