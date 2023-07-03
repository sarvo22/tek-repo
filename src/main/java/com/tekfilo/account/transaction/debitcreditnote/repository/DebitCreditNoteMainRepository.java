package com.tekfilo.account.transaction.debitcreditnote.repository;

import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DebitCreditNoteMainRepository
        extends JpaRepository<DebitCreditNoteMainEntity,Integer>, JpaSpecificationExecutor {

    @Modifying
    @Transactional
    @Query("update DebitCreditNoteMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);
}
