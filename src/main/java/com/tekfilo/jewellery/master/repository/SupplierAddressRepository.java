package com.tekfilo.jewellery.master.repository;

import com.tekfilo.jewellery.master.SupplierAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierAddressRepository extends JpaRepository<SupplierAddressEntity, Integer> {
}
