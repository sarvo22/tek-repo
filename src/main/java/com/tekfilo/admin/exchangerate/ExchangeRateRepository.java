package com.tekfilo.admin.exchangerate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, Integer> {

    @Query("select e from ExchangeRateEntity e where e.isDeleted = 0 and e.currency = :currency order by exchangeRateDate desc")
    List<ExchangeRateEntity> findByCode(@Param("currency") String currency);

    @Query("select e from ExchangeRateEntity e where e.isDeleted = 0 and e.currency = :currency and e.exchangeRateDate <= :date order by exchangeRateDate desc")
    List<ExchangeRateEntity> findAllByCodeAndDate(String currency, Date date);
}
