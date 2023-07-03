package com.tekfilo.sso.plan.repository;

import com.tekfilo.sso.plan.entity.CompanySubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanySubscriptionRepository extends JpaRepository<CompanySubscription, Integer>, JpaSpecificationExecutor {
    @Query("FROM CompanySubscription c where c.isDeleted = 0 and c.companyId = :companyId")
    List<CompanySubscription> findAllByCompanyId(Integer companyId);
}
