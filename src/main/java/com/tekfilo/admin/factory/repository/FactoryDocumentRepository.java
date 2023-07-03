package com.tekfilo.admin.factory.repository;

import com.tekfilo.admin.factory.entity.FactoryDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactoryDocumentRepository extends JpaRepository<FactoryDocumentEntity, Integer>, JpaSpecificationExecutor {
    @Query("select d from FactoryDocumentEntity d where d.isDeleted = 0 and d.factoryId = :factoryId")
    List<FactoryDocumentEntity> findDocumentsByFactoryId(Integer factoryId);
}
