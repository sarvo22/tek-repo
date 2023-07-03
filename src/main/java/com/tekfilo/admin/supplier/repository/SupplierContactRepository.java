package com.tekfilo.admin.supplier.repository;

import com.tekfilo.admin.supplier.entity.SupplierContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierContactRepository extends JpaRepository<SupplierContactEntity, Integer>, JpaSpecificationExecutor {
    @Query("select c from SupplierContactEntity c where c.isDeleted = 0 and c.supplierId = :supplierId")
    List<SupplierContactEntity> findContactsBySupplierId(Integer supplierId);
}
