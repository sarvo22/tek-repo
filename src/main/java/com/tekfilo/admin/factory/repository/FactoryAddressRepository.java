package com.tekfilo.admin.factory.repository;

import com.tekfilo.admin.factory.entity.FactoryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactoryAddressRepository extends JpaRepository<FactoryAddressEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from FactoryAddressEntity a where a.isDeleted = 0 and a.factoryId = :id")
    List<FactoryAddressEntity> findAddressByFactoryId(Integer id);
}
