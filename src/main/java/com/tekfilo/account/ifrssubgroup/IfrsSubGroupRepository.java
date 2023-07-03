package com.tekfilo.account.ifrssubgroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IfrsSubGroupRepository extends JpaRepository<IfrsSubGroupEntity, Integer>, JpaSpecificationExecutor {

    @Query("select d from IfrsSubGroupEntity d where d.isDeleted = 0 ")
    Page<IfrsSubGroupEntity> findAllGroups(Pageable pageable);
}
