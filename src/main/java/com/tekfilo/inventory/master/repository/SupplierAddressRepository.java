package com.tekfilo.inventory.master.repository;

import com.tekfilo.inventory.master.SupplierAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierAddressRepository extends JpaRepository<SupplierAddressEntity, Integer> {
}
