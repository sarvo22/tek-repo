package com.tekfilo.jewellery.issuereceive.repository;

import com.tekfilo.jewellery.issuereceive.entity.BaggingReturnIrrespectiveMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggingReturnIrRespectiveMainRepository extends JpaRepository<BaggingReturnIrrespectiveMainEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from BaggingReturnIrrespectiveMainEntity a where a.isDeleted = 0 and a.id in (:ids)")
    List<BaggingReturnIrrespectiveMainEntity> findAllMainByMainIds(List<Integer> ids);
}
