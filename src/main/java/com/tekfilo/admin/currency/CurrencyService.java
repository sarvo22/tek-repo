package com.tekfilo.admin.currency;

import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CurrencyService implements ICurrencyService {

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    CurrencyCompanyMapService currencyCompanyMapService;

    @Override
    public List<CurrencyEntity> findAll() {
        return currencyRepository.findAllCurrencies();
    }

    @Override
    public CurrencyEntity findByCode(String currencyCode) {
        return currencyRepository.findByCode(currencyCode);
    }

    @Override
    public void updateCurrency(CurrencyDto currencyDto) throws Exception {
        currencyRepository.save(convertToEntity(currencyDto));
    }

    @Override
    public void createCurrency(CurrencyDto currencyDto) throws Exception {
        boolean currencyExists = false;
        boolean currencyMappingExists = false;
        CurrencyEntity entity = currencyRepository.findByCode(currencyDto.getCode());
        if (Optional.ofNullable(entity.getCode()).isPresent()) {
            currencyExists = true;
        }
        List<CurrencyCompanyMapEntity> currencyCompanyMapEntityList = this.currencyCompanyMapService.findAllCurrencyByCompanyAndCode(CompanyContext.getCurrentCompany(), currencyDto.getCode());

        if (currencyCompanyMapEntityList.size() > 0) {
            currencyMappingExists = true;
        }
        if (currencyExists && currencyMappingExists)
            throw new RuntimeException("Currency already exists and mapped with Company");

        if (!currencyExists) {
            currencyRepository.save(convertToEntity(currencyDto));
        }

        if (!currencyMappingExists) {
            currencyCompanyMapService.save(CompanyContext.getCurrentCompany(), currencyDto.getCode(), String.valueOf(UserContext.getLoggedInUser()));
        }
    }

    private CurrencyEntity convertToEntity(CurrencyDto currencyDto) {
        CurrencyEntity entity = new CurrencyEntity();
        BeanUtils.copyProperties(currencyDto, entity);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setSequence(0);
        entity.setIsDeleted(0);
        entity.setIsLocked(0);
        return entity;
    }
}
