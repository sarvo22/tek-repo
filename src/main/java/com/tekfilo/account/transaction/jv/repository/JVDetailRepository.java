package com.tekfilo.account.transaction.jv.repository;

import com.tekfilo.account.transaction.bankpaymentreceipt.entity.BankPaymentReceiptDetailEntity;
import com.tekfilo.account.transaction.jv.entity.JVDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JVDetailRepository extends JpaRepository<JVDetailEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM JVDetailEntity where jvMainId = :id and isDeleted = 0 ")
    List<JVDetailEntity> findAllByMainId(Integer id);

}
