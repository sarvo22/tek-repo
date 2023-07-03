package com.tekfilo.admin.customer.repository;

import com.tekfilo.admin.customer.entity.CustomerLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerLimitRepository extends JpaRepository<CustomerLimitEntity, Integer>, JpaSpecificationExecutor {
    @Query("select l from CustomerLimitEntity l where l.isDeleted = 0 and l.customerId = :customerId")
    List<CustomerLimitEntity> findLimitsByCustomerId(Integer customerId);
}
