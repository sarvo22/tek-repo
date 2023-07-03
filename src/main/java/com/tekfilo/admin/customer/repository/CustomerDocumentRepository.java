package com.tekfilo.admin.customer.repository;

import com.tekfilo.admin.customer.entity.CustomerDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDocumentRepository extends JpaRepository<CustomerDocumentEntity, Integer>, JpaSpecificationExecutor {
    @Query("select d from CustomerDocumentEntity d where d.isDeleted = 0 and d.customerId = :customerId")
    List<CustomerDocumentEntity> findDocumentsByCustomerId(Integer customerId);
}
