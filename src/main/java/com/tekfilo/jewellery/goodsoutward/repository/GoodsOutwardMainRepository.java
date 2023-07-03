package com.tekfilo.jewellery.goodsoutward.repository;

import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardMainEntity;
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
public interface GoodsOutwardMainRepository extends JpaRepository<GoodsOutwardMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("select sm from GoodsOutwardMainEntity sm where sm.isDeleted = 0")
    Page<GoodsOutwardMainEntity> findAllInvoiceMain(Pageable pageable);

    @Query("FROM GoodsOutwardMainEntity")
    List<GoodsOutwardMainEntity> findMain();

    @Query("select sm from GoodsOutwardMainEntity sm where sm.isDeleted = 0 and sm.partyId = :partyId ")
    List<GoodsOutwardMainEntity> getPendingInvoiceList(Integer partyId);


    @Query("FROM GoodsOutwardMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<GoodsOutwardMainEntity> findAllMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update GoodsOutwardMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIds(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;


    @Modifying
    @Transactional
    @Query("update GoodsOutwardMainEntity m set m.documentUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);

}
