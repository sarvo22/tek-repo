package com.tekfilo.sso.apiconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiConfigRepository extends JpaRepository<ApiConfigEntity, Integer> {

    @Query("select api from ApiConfigEntity api where api.isDeleted = 0 and api.apiId = :apiId and api.companyId = :companyId")
    List<ApiConfigEntity> findByApiAndCompanyId(Integer apiId, Integer companyId);
}
