package com.tekfilo.account.transaction.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingMainRepository extends JpaRepository<PostingMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM PostingMainEntity m where m.isDeleted = 0 and m.invoiceType = :invoiceType and m.invId = :invoiceId and m.companyId = :currentCompany")
    List<PostingMainEntity> findAllByVoucher(String invoiceType, Integer invoiceId, Integer currentCompany);

    @Query("FROM PostingMainEntity m where m.isDeleted = 0 and m.invoiceType in(:types) and m.invId in (:ids) and m.companyId = :currentCompany")
    List<PostingMainEntity> findAllByVoucherList(List<String> types, List<Integer> ids, Integer currentCompany);
}
