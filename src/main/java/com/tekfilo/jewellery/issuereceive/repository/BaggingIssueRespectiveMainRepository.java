package com.tekfilo.jewellery.issuereceive.repository;

import com.tekfilo.jewellery.issuereceive.entity.BaggingIssueRespectiveMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggingIssueRespectiveMainRepository extends JpaRepository<BaggingIssueRespectiveMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select a from BaggingIssueRespectiveMainEntity a where a.isDeleted = 0 and a.id in (:ids)")
    List<BaggingIssueRespectiveMainEntity> findAllMainByMainIds(List<Integer> ids);
}
