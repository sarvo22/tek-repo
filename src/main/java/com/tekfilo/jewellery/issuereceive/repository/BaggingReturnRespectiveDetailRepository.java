package com.tekfilo.jewellery.issuereceive.repository;

import com.tekfilo.jewellery.issuereceive.entity.BaggingReturnRespectiveDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggingReturnRespectiveDetailRepository extends JpaRepository<BaggingReturnRespectiveDetailEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM BaggingReturnRespectiveDetailEntity where isDeleted = 0 and invId = :invId")
    List<BaggingReturnRespectiveDetailEntity> findAllByMainId(Integer invId);

    @Query("FROM BaggingReturnRespectiveDetailEntity where isDeleted = 0 and invId in (:ids)")
    List<BaggingReturnRespectiveDetailEntity> findAllByDetailByMainIds(List<Integer> ids);
}
