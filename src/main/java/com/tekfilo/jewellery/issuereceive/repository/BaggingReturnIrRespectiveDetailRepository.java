package com.tekfilo.jewellery.issuereceive.repository;

import com.tekfilo.jewellery.issuereceive.entity.BaggingReturnIrrespectiveDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggingReturnIrRespectiveDetailRepository extends JpaRepository<BaggingReturnIrrespectiveDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM BaggingReturnIrrespectiveDetailEntity where isDeleted = 0 and invId = :invId")
    List<BaggingReturnIrrespectiveDetailEntity> findAllByMainId(Integer invId);

    @Query("FROM BaggingReturnIrrespectiveDetailEntity where isDeleted = 0 and invId in (:ids)")
    List<BaggingReturnIrrespectiveDetailEntity> findAllByDetailByMainIds(List<Integer> ids);
}
