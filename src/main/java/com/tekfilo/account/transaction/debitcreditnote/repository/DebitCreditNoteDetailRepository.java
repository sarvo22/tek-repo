package com.tekfilo.account.transaction.debitcreditnote.repository;

import com.tekfilo.account.transaction.debitcreditnote.entity.DebitCreditNoteDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebitCreditNoteDetailRepository extends JpaRepository<DebitCreditNoteDetailEntity,Integer>, JpaSpecificationExecutor {

    @Query("select d from DebitCreditNoteDetailEntity d where d.isDeleted = 0 and d.invId = :id")
    List<DebitCreditNoteDetailEntity> findAllByMainId(Integer id);
}
