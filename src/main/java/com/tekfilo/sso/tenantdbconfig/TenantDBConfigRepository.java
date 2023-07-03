package com.tekfilo.sso.tenantdbconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TenantDBConfigRepository extends JpaRepository<TenantDBConfigEntity, Integer> {

    @Query("select t from TenantDBConfigEntity t where userId = :userId")
    List<TenantDBConfigEntity> getTenantUIDByUser(@Param("userId") Integer userId);

    @Query("FROM TenantDBConfigEntity te where te.userId = :userId and te.companyId = :senderCompanyId")
    List<TenantDBConfigEntity> getTenantUIDByCompanyId(Integer senderCompanyId, Integer userId);

    @Modifying
    @Transactional
    @Query("update TenantDBConfigEntity set isLocked = 0 where userId = :userId and isLocked = 1")
    void updateLockedStatus(Integer userId);

    @Query("FROM TenantDBConfigEntity where isLocked = 1 and userId = :userId")
    List<TenantDBConfigEntity> findAllLockedTenantsByUserID(Integer userId);
}
