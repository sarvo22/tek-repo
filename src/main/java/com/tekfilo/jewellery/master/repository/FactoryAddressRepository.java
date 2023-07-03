package com.tekfilo.jewellery.master.repository;

import com.tekfilo.jewellery.master.FactoryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryAddressRepository extends JpaRepository<FactoryAddressEntity, Integer> {
}
