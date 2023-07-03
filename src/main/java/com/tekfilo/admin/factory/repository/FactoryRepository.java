package com.tekfilo.admin.factory.repository;

import com.tekfilo.admin.factory.entity.FactoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactoryRepository extends JpaRepository<FactoryEntity, Integer>, JpaSpecificationExecutor {

    @Query("SELECT e from FactoryEntity e where lower(e.factoryCode) LIKE :keyword% or lower(factoryName) LIKE :keyword%")
    List<FactoryEntity> getFactoryList(@Param("keyword") String keyword);
}
