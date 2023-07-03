package com.tekfilo.admin.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {

    @Query("select c from CurrencyEntity c where c.isDeleted = 0 ")
    List<CurrencyEntity> findAllCurrencies();

    @Query("select c from CurrencyEntity c where c.isDeleted = 0 and c.code = :currencyCode")
    CurrencyEntity findByCode(String currencyCode);

}
