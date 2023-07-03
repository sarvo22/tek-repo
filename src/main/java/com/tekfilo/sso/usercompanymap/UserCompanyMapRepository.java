package com.tekfilo.sso.usercompanymap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCompanyMapRepository extends JpaRepository<UserCompanyMapEntity, Integer> {

    @Query("select a from UserCompanyMapEntity a where a.isDeleted = 0 and a.userId= :userId")
    List<UserCompanyMapEntity> findUserCompaniesByUserId(Integer userId);

    @Query("select a from UserCompanyMapEntity a where a.isDeleted = 0 and a.userId = :userId and isDefault = 1")
    List<UserCompanyMapEntity> findDefaultCompany(Integer userId);

    @Query("select c from UserCompanyMapEntity c where c.isDeleted = 0 and c.companyId = :companyId")
    List<UserCompanyMapEntity> findAllUserCompanyList(Integer companyId);
}
