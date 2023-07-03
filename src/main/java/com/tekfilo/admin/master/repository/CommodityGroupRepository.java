package com.tekfilo.admin.master.repository;

import com.tekfilo.admin.master.CommodityGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityGroupRepository extends JpaRepository<CommodityGroupEntity, Integer>, JpaSpecificationExecutor {
}
