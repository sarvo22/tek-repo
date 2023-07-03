package com.tekfilo.admin.supplier.repository;

import com.tekfilo.admin.supplier.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Integer>, JpaSpecificationExecutor {

    @Query("SELECT e from SupplierEntity e where lower(e.supplierCode) LIKE :keyword% or lower(supplierName) LIKE :keyword%")
    List<SupplierEntity> getSupplierList(@Param("keyword") String keyword);
}
