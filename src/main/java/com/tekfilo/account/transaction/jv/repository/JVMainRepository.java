package com.tekfilo.account.transaction.jv.repository;

import com.tekfilo.account.transaction.jv.entity.JVMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JVMainRepository extends JpaRepository<JVMainEntity, Integer>, JpaSpecificationExecutor {

    @Modifying
    @Transactional
    @Query("update JVMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);
}
