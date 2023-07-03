package com.tekfilo.jewellery.bintransfer.repository;


import com.tekfilo.jewellery.bintransfer.entity.BinTransferDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinTransferDetailRepository extends JpaRepository<BinTransferDetailEntity, Integer> {
    @Query("select de from BinTransferDetailEntity de where de.isDeleted = 0 and  transferId = :id")
    List<BinTransferDetailEntity> findAllDetailByMainId(Integer id);
}
