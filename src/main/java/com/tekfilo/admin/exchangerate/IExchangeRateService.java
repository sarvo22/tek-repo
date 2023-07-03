package com.tekfilo.admin.exchangerate;

import java.sql.Date;
import java.util.List;

public interface IExchangeRateService {
    List<ExchangeRateEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection);

    ExchangeRateEntity save(ExchangeRateDto exchangeRateDto) throws Exception;

    void modify(ExchangeRateDto exchangeRateDto) throws Exception;

    ExchangeRateEntity findById(Integer id);

    void remove(ExchangeRateEntity entity) throws Exception;

    List<ExchangeRateEntity> findByCode(String currency);

    ExchangeRateEntity getLastExchange(String currencyCode);

    ExchangeRateEntity findByCodeAndDate(String currency, Date date);
}
