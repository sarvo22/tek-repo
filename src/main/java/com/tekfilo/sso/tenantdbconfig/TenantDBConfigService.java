package com.tekfilo.sso.tenantdbconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TenantDBConfigService {

    @Autowired
    TenantDBConfigRepository tenantRepository;


    public String getTenantUIDByUserId(Integer userId) throws Exception {
        List<TenantDBConfigEntity> entities = tenantRepository.getTenantUIDByUser(userId);
        if (entities.size() == 0)
            throw new Exception("Unable to find Tenant ID");
        if (entities.get(0).getTenantUID() == null)
            throw new Exception("Unable to find Tenant ID");
        return entities.get(0).getTenantUID().toLowerCase();
    }

    public String getTenantUIDByUserAndCompanyId(Integer userId, Integer companyId) throws Exception {
        List<TenantDBConfigEntity> entities = tenantRepository.getTenantUIDByCompanyId(companyId, userId);
        if (entities.size() == 0)
            throw new Exception("Unable to find Tenant ID");
        if (entities.get(0).getTenantUID() == null)
            throw new Exception("Unable to find Tenant ID");
        return entities.get(0).getTenantUID().toLowerCase();
    }

}
