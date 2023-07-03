package com.tekfilo.account.ifrsgroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IfrsGroupRepository extends JpaRepository<IfrsGroupEntity, Integer>, JpaSpecificationExecutor {

    @Query("select d from IfrsGroupEntity d where d.isDeleted = 0 ")
    Page<IfrsGroupEntity> findAllGroups(Pageable pageable);
}
