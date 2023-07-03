package com.tekfilo.admin.supplier.repository;

import com.tekfilo.admin.supplier.entity.SupplierAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierAddressRepository extends JpaRepository<SupplierAddressEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from SupplierAddressEntity a where a.isDeleted = 0 and a.supplierId = :id")
    List<SupplierAddressEntity> findAddressBySupplierId(Integer id);
}
