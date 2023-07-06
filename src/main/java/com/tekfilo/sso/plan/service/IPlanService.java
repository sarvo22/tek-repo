package com.tekfilo.sso.plan.service;

import com.tekfilo.sso.plan.entity.AppEntity;
import com.tekfilo.sso.plan.entity.CompanySubscription;
import com.tekfilo.sso.plan.entity.Subscription;
import com.tekfilo.sso.plan.entity.SubscriptionPrivilege;

import java.util.List;

public interface IPlanService {

    List<AppEntity> getTekfiloApps();

    List<Subscription> getTekfiloSubscriptionPlans();

    List<CompanySubscription> getCompanySubscription(Integer companyId);

    List<SubscriptionPrivilege> getSubscriptionPrivileges(Integer subscriptionId);
}
