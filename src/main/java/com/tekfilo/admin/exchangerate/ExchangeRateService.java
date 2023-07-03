package com.tekfilo.admin.exchangerate;

import com.tekfilo.admin.multitenancy.CompanyContext;
import com.tekfilo.admin.multitenancy.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class ExchangeRateService implements IExchangeRateService {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;


    @Override
    public List<ExchangeRateEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection) {
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ? PageRequest.of(pageNo, pageSize, Sort.by(sortName).ascending()) :
                PageRequest.of(pageNo, pageSize, Sort.by(sortName).descending());
        Page<ExchangeRateEntity> pagedList = exchangeRateRepository.findAll(pageable);
        return pagedList.toList();
    }

    @Override
    public ExchangeRateEntity save(ExchangeRateDto exchangeRateDto) throws Exception {
        return exchangeRateRepository.save(convertToEntity(exchangeRateDto));
    }

    private ExchangeRateEntity convertToEntity(ExchangeRateDto exchangeRateDto) {
        ExchangeRateEntity entity = new ExchangeRateEntity();
        BeanUtils.copyProperties(exchangeRateDto, entity);
        entity.setExchangeRateDate(Date.valueOf(exchangeRateDto.getExchangeRateDate()));
        entity.setSequence(0);
        entity.setIsLocked(0);
        entity.setCompanyId(CompanyContext.getCurrentCompany());
        entity.setCreatedBy(UserContext.getLoggedInUser());
        entity.setModifiedBy(UserContext.getLoggedInUser());
        entity.setIsDeleted(exchangeRateDto.getIsDeleted() == null ? 0 : exchangeRateDto.getIsDeleted());
        return entity;
    }

    @Override
    public void modify(ExchangeRateDto exchangeRateDto) throws Exception {
        exchangeRateRepository.save(convertToEntity(exchangeRateDto));
    }

    @Override
    public ExchangeRateEntity findById(Integer id) {
        return exchangeRateRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(ExchangeRateEntity entity) throws Exception {
        exchangeRateRepository.save(entity);
    }

    @Override
    public List<ExchangeRateEntity> findByCode(String currency) {
        return exchangeRateRepository.findByCode(currency);
    }

    @Override
    public ExchangeRateEntity getLastExchange(String currencyCode) {

        List<ExchangeRateEntity> exchangeRateEntityList = this.findByCode(currencyCode);
        if (exchangeRateEntityList.size() > 0)
            return exchangeRateEntityList.get(0);
        return new ExchangeRateEntity();
    }

    @Override
    public ExchangeRateEntity findByCodeAndDate(String currency, Date date) {
        List<ExchangeRateEntity> exchangeRateEntityList = this.exchangeRateRepository.findAllByCodeAndDate(currency, date);
        return exchangeRateEntityList.size() > 0 ? exchangeRateEntityList.get(0) : new ExchangeRateEntity();
    }
}
