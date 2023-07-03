package com.tekfilo.jewellery.issuereceive.repository;

import com.tekfilo.jewellery.issuereceive.entity.BaggingIssueRespectiveDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaggingIssueRespectiveDetailRepository extends JpaRepository<BaggingIssueRespectiveDetailEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM BaggingIssueRespectiveDetailEntity where isDeleted = 0 and invId = :invId")
    List<BaggingIssueRespectiveDetailEntity> findAllByMainId(Integer invId);

    @Query("FROM BaggingIssueRespectiveDetailEntity where isDeleted = 0 and invId in (:ids)")
    List<BaggingIssueRespectiveDetailEntity> findAllByDetailByMainIds(List<Integer> ids);
}
