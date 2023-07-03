package com.tekfilo.jewellery.goodsinward.repository;

import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardDetailEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GoodsInwardDetailRepository extends JpaRepository<GoodsInwardDetailEntity, Integer> {
    @Query("select a from GoodsInwardDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<GoodsInwardDetailEntity> findAllDetail(Integer id);

    @Query("FROM GoodsInwardDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<GoodsInwardDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update GoodsInwardDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
