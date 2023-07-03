package com.tekfilo.inventory.master.repository;

import com.tekfilo.inventory.master.CustomerAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddressEntity, Integer> {
}
