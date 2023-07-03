package com.tekfilo.jewellery.issuereceive.repository;

import com.tekfilo.jewellery.issuereceive.entity.BaggingReturnRespectiveMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggingReturnRespectiveMainRepository extends JpaRepository<BaggingReturnRespectiveMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select a from BaggingReturnRespectiveMainEntity a where a.isDeleted = 0 and a.id in (:ids)")
    List<BaggingReturnRespectiveMainEntity> findAllMainByMainIds(List<Integer> ids);
}
