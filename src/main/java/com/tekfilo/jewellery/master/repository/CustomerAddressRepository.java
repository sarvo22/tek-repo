package com.tekfilo.jewellery.master.repository;

import com.tekfilo.jewellery.master.CustomerAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddressEntity, Integer> {
}
