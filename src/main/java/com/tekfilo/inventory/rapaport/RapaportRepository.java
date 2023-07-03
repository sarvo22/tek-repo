package com.tekfilo.inventory.rapaport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RapaportRepository extends JpaRepository<RapaportEntity, Integer>, JpaSpecificationExecutor {
}
