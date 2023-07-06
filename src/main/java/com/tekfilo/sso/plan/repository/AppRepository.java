package com.tekfilo.sso.plan.repository;

import com.tekfilo.sso.plan.entity.AppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<AppEntity, Integer>, JpaSpecificationExecutor {
}
