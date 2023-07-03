package com.tekfilo.jewellery.issuereceive.repository;

import com.tekfilo.jewellery.issuereceive.entity.BaggingIssueIrrespectiveMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggingIssueIrRespectiveMainRepository extends JpaRepository<BaggingIssueIrrespectiveMainEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from BaggingIssueIrrespectiveMainEntity a where a.isDeleted = 0 and a.id in (:ids)")
    List<BaggingIssueIrrespectiveMainEntity> findAllMainByMainIds(List<Integer> ids);
}
