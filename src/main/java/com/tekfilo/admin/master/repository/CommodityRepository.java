package com.tekfilo.admin.master.repository;

import com.tekfilo.admin.master.CommodityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityRepository extends JpaRepository<CommodityEntity, Integer>, JpaSpecificationExecutor {
}
