package com.tekfilo.admin.currency;

import java.util.List;

public interface ICurrencyService {

    List<CurrencyEntity> findAll();

    CurrencyEntity findByCode(String currencyCode);

    void updateCurrency(CurrencyDto currencyDto) throws Exception;

    void createCurrency(CurrencyDto currencyDto) throws Exception;
}
