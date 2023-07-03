package com.tekfilo.sps.ibot.repository;

import com.tekfilo.sps.ibot.entities.ApiConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiConfigRepository extends JpaRepository<ApiConfigEntity, Integer> {

    @Query(value = " SELECT current_database()", nativeQuery = true)
    String findCurrentDatabase();
}
