package com.tekfilo.admin.customer.repository;

import com.tekfilo.admin.customer.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>, JpaSpecificationExecutor {

    @Query("select cus from CustomerEntity cus where cus.isDeleted = 0 ")
    Page<CustomerEntity> findAll(Pageable pageable);

    @Query("SELECT e from CustomerEntity e where lower(e.customerCode) LIKE :keyword% or lower(customerName) LIKE :keyword%")
    List<CustomerEntity> getCustomerList(@Param("keyword") String keyword);
}
