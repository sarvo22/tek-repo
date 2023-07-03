package com.tekfilo.admin.metalrate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MetalRateRepository extends JpaRepository<MetalRateEntity, Integer>, JpaSpecificationExecutor {
}
