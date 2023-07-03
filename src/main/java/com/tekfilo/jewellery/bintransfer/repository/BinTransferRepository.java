package com.tekfilo.jewellery.bintransfer.repository;

import com.tekfilo.jewellery.bintransfer.entity.BinTransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BinTransferRepository extends JpaRepository<BinTransferEntity, Integer>, JpaSpecificationExecutor {

}
