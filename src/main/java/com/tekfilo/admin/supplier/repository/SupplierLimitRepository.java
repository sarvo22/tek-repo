package com.tekfilo.admin.supplier.repository;

import com.tekfilo.admin.supplier.entity.SupplierLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierLimitRepository extends JpaRepository<SupplierLimitEntity, Integer>, JpaSpecificationExecutor {
    @Query("select l from SupplierLimitEntity l where l.isDeleted = 0 and l.supplierId = :supplierId")
    List<SupplierLimitEntity> findLimitsBySupplierId(Integer supplierId);
}
