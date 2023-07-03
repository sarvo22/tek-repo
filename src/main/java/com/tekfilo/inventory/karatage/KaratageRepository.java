package com.tekfilo.inventory.karatage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KaratageRepository extends JpaRepository<KaratageEntity, Integer>, JpaSpecificationExecutor {
    @Query("select count(1) from KaratageEntity where lower(karatageName) = :karatageName")
    int checkNameExists(String karatageName);
}
