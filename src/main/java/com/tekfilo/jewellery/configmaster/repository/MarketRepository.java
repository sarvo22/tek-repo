package com.tekfilo.jewellery.configmaster.repository;

import com.tekfilo.jewellery.configmaster.entity.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Integer>, JpaSpecificationExecutor {
}
