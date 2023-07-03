package com.tekfilo.sso.plan.repository;

import com.tekfilo.sso.plan.entity.SubscriptionPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPrivilegeRepository extends JpaRepository<SubscriptionPrivilege, Integer>, JpaSpecificationExecutor {
    @Query("FROM SubscriptionPrivilege p where p.isDeleted = 0 and p.subscriptionId = :subscriptionId")
    List<SubscriptionPrivilege> findAllBySubscriptionPrivilegeBySubscription(Integer subscriptionId);
}
