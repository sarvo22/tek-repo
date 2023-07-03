package com.tekfilo.inventory.bin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BinRepository extends JpaRepository<BinEntity, Integer>, JpaSpecificationExecutor {

    @Query("select count(1) from BinEntity where lower(binName) = :binName")
    int checkNameExists(String binName);
}
