package com.tekfilo.sso.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM ReportEntity where isDeleted = 0 and isEnabled = 1 and subscriptionId = :subscriptionId")
    List<ReportEntity> findAllBySubscriptionId(Integer subscriptionId);

}
