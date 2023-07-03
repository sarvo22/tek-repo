package com.tekfilo.inventory.bintransfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BinTransferRepository extends JpaRepository<BinTransferEntity, Integer>, JpaSpecificationExecutor {

}
