package com.tekfilo.account.group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Integer>, JpaSpecificationExecutor {

    @Query("select d from GroupEntity d where d.isDeleted = 0 ")
    Page<GroupEntity> findAllGroups(Pageable pageable);
}
