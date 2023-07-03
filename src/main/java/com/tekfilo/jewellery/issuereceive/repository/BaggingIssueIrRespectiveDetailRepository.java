package com.tekfilo.jewellery.issuereceive.repository;

import com.tekfilo.jewellery.issuereceive.entity.BaggingIssueIrrespectiveDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggingIssueIrRespectiveDetailRepository extends JpaRepository<BaggingIssueIrrespectiveDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM BaggingIssueIrrespectiveDetailEntity where isDeleted = 0 and invId = :invId")
    List<BaggingIssueIrrespectiveDetailEntity> findAllByMainId(Integer invId);

    @Query("FROM BaggingIssueIrrespectiveDetailEntity where isDeleted = 0 and invId in (:ids)")
    List<BaggingIssueIrrespectiveDetailEntity> findAllByDetailByMainIds(List<Integer> ids);
}
