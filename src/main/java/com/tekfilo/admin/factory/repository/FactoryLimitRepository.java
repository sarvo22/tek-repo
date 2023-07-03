package com.tekfilo.admin.factory.repository;

import com.tekfilo.admin.factory.entity.FactoryLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactoryLimitRepository extends JpaRepository<FactoryLimitEntity, Integer>, JpaSpecificationExecutor {
    @Query("select l from FactoryLimitEntity l where l.isDeleted = 0 and l.factoryId = :factoryId")
    List<FactoryLimitEntity> findLimitsByFactoryId(Integer factoryId);
}
