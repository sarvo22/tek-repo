package com.tekfilo.admin.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyCompanyMapRepository extends JpaRepository<CurrencyCompanyMapEntity, Integer> {
    @Query("select a from CurrencyCompanyMapEntity a where a.isDeleted = 0 and a.companyId = :companyId")
    List<CurrencyCompanyMapEntity> findAllCurrencyByCompanyId(Integer companyId);

    @Query("select a from CurrencyCompanyMapEntity a where a.isDeleted = 0 and a.companyId = :companyId and currencyCode = :code")
    List<CurrencyCompanyMapEntity> findAllCurrencyByCompanyAndCode(Integer companyId, String code);
}
