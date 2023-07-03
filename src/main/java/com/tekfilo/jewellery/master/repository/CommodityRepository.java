package com.tekfilo.jewellery.master.repository;

import com.tekfilo.jewellery.master.CommodityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityRepository extends JpaRepository<CommodityEntity, Integer>, JpaSpecificationExecutor {
}
