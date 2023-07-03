package com.tekfilo.sso.plan;

import com.tekfilo.sso.plan.entity.AppEntity;
import com.tekfilo.sso.plan.entity.CompanySubscription;
import com.tekfilo.sso.plan.entity.Subscription;
import com.tekfilo.sso.plan.service.impl.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sso/tekfilo/plan")
public class PlanController {

    @Autowired
    PlanService planService;

    @GetMapping("/apps")
    public ResponseEntity<List<AppEntity>> getApps() {
        return new ResponseEntity<List<AppEntity>>(this.planService.getTekfiloApps(), HttpStatus.OK);
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getSubscriptions() {
        return new ResponseEntity<List<Subscription>>(this.planService.getTekfiloSubscriptionPlans(), HttpStatus.OK);
    }

    @GetMapping("/companysubscription/{companyid}")
    public ResponseEntity<List<CompanySubscription>> getCompanySubscription(@PathVariable("companyid") Integer companyId) {
        return new ResponseEntity<List<CompanySubscription>>(this.planService.getCompanySubscription(companyId), HttpStatus.OK);
    }

}
