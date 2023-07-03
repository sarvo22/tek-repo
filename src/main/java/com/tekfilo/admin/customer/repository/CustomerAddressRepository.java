package com.tekfilo.admin.customer.repository;

import com.tekfilo.admin.customer.entity.CustomerAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddressEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from CustomerAddressEntity a where a.isDeleted = 0 and a.customerId = :id")
    List<CustomerAddressEntity> findAddressByCustomerId(Integer id);
}
