package com.tekfilo.sso.plan.service.impl;

import com.tekfilo.sso.plan.entity.AppEntity;
import com.tekfilo.sso.plan.entity.CompanySubscription;
import com.tekfilo.sso.plan.entity.Subscription;
import com.tekfilo.sso.plan.entity.SubscriptionPrivilege;
import com.tekfilo.sso.plan.repository.AppRepository;
import com.tekfilo.sso.plan.repository.CompanySubscriptionRepository;
import com.tekfilo.sso.plan.repository.SubscriptionPrivilegeRepository;
import com.tekfilo.sso.plan.repository.SubscriptionRepository;
import com.tekfilo.sso.plan.service.IPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
public class PlanService implements IPlanService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    CompanySubscriptionRepository companySubscriptionRepository;

    @Autowired
    SubscriptionPrivilegeRepository subscriptionPrivilegeRepository;

    @Override
    public List<AppEntity> getTekfiloApps() {
        return this.appRepository.findAll();
    }

    @Override
    public List<Subscription> getTekfiloSubscriptionPlans() {
        return this.subscriptionRepository.findAll();
    }

    @Override
    public List<CompanySubscription> getCompanySubscription(Integer companyId) {
        return this.companySubscriptionRepository.findAllByCompanyId(companyId);
    }

    @Override
    public List<SubscriptionPrivilege> getSubscriptionPrivileges(Integer subscriptionId) {
        return this.subscriptionPrivilegeRepository.findAllBySubscriptionPrivilegeBySubscription(subscriptionId);
    }

}
