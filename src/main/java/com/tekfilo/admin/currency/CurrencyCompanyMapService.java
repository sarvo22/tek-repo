package com.tekfilo.admin.currency;

import com.tekfilo.admin.multitenancy.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CurrencyCompanyMapService {

    @Autowired
    CurrencyCompanyMapRepository currencyCompanyMapRepository;

    public List<CurrencyCompanyMapEntity> findAllCurrenciesByCompanyId(Integer companyId) {
        return currencyCompanyMapRepository.findAllCurrencyByCompanyId(companyId);
    }

    public void save(Integer companyId, String currencyCode, String operateBy) throws Exception {
        CurrencyCompanyMapEntity entity = new CurrencyCompanyMapEntity();
        entity.setCurrencyCode(currencyCode);
        entity.setCompanyId(companyId);
        entity.setIsDefault(0);
        entity.setIsDeleted(0);
        entity.setCreatedBy(Integer.parseInt(operateBy));
        entity.setModifiedBy(Integer.parseInt(operateBy));
        entity.setIsLocked(0);
        currencyCompanyMapRepository.save(entity);
    }

    public List<CurrencyCompanyMapEntity> findAllCurrencyByCompanyAndCode(Integer currentCompany, String code) {
        return this.currencyCompanyMapRepository.findAllCurrencyByCompanyAndCode(currentCompany, code);
    }

    public void deleteCompanyMap(Integer mapId) throws Exception {
        CurrencyCompanyMapEntity entity = this.currencyCompanyMapRepository.findById(mapId).get();
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(1);
        this.currencyCompanyMapRepository.save(entity);
    }
}
