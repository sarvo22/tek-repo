package com.tekfilo.inventory.master.repository;

import com.tekfilo.inventory.master.AccMasterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccMasterRepository extends JpaRepository<AccMasterEntity, Integer>, JpaSpecificationExecutor {

    @Query("select d from AccMasterEntity d where d.isDeleted = 0 ")
    Page<AccMasterEntity> findAllAccMasters(Pageable pageable);

    @Query("select p from AccMasterEntity p where p.isDeleted = 0 and p.companyId  = :companyId and lower(p.accountName) LIKE :searchKey% ")
    List<AccMasterEntity> findByAccountName(String searchKey, Integer companyId);
}
