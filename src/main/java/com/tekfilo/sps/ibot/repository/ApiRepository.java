package com.tekfilo.sps.ibot.repository;

import com.tekfilo.sps.ibot.entities.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<ApiEntity, Integer> {
}
