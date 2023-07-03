package com.tekfilo.account.transaction.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingDetailRepository extends JpaRepository<PostingDetailEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM PostingDetailEntity where accountId = :accountId")
    List<PostingDetailEntity> findAllByAccountId(Integer accountId);
}
