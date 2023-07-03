package com.tekfilo.account.subgroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubGroupRepository extends JpaRepository<SubGroupEntity, Integer>, JpaSpecificationExecutor {

    @Query("select d from SubGroupEntity d where d.isDeleted = 0 ")
    Page<SubGroupEntity> findAllGroups(Pageable pageable);
}
