package com.tekfilo.jewellery.goodsinward.repository;

import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardMainEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceMainEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GoodsInwardMainRepository extends JpaRepository<GoodsInwardMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select sm from GoodsInwardMainEntity sm where sm.isDeleted = 0")
    Page<GoodsInwardMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM GoodsInwardMainEntity")
    List<GoodsInwardMainEntity> findMain();

    @Query("select sm from GoodsInwardMainEntity sm where sm.isDeleted = 0 and sm.partyId = :partyId ")
    List<GoodsInwardMainEntity> getPendingInvoiceList(Integer partyId);

    @Query("FROM GoodsInwardMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<GoodsInwardMainEntity> findAllMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update GoodsInwardMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update GoodsInwardMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

}
