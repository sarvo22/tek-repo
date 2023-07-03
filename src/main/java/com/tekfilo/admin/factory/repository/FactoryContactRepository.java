package com.tekfilo.admin.factory.repository;

import com.tekfilo.admin.factory.entity.FactoryContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactoryContactRepository extends JpaRepository<FactoryContactEntity, Integer>, JpaSpecificationExecutor {
    @Query("select c from FactoryContactEntity c where c.isDeleted = 0 and c.factoryId = :factoryId")
    List<FactoryContactEntity> findContactsByFactoryId(Integer factoryId);
}
