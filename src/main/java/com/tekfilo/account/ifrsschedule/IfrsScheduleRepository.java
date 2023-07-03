package com.tekfilo.account.ifrsschedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IfrsScheduleRepository extends JpaRepository<IfrsScheduleEntity, Integer>, JpaSpecificationExecutor {

    @Query("select d from IfrsScheduleEntity d where d.isDeleted = 0 ")
    Page<IfrsScheduleEntity> findAllScheduls(Pageable pageable);
}
