package com.tekfilo.admin.customer.repository;

import com.tekfilo.admin.customer.entity.CustomerContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerContactRepository extends JpaRepository<CustomerContactEntity, Integer>, JpaSpecificationExecutor {
    @Query("select c from CustomerContactEntity c where c.isDeleted = 0 and c.customerId = :customerId")
    List<CustomerContactEntity> findContactsByCustomerId(Integer customerId);
}
