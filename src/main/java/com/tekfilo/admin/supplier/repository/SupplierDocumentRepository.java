package com.tekfilo.admin.supplier.repository;

import com.tekfilo.admin.supplier.entity.SupplierDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierDocumentRepository extends JpaRepository<SupplierDocumentEntity, Integer>, JpaSpecificationExecutor {
    @Query("select d from SupplierDocumentEntity d where d.isDeleted = 0 and d.supplierId = :supplierId")
    List<SupplierDocumentEntity> findDocumentsBySupplierId(Integer supplierId);
}
