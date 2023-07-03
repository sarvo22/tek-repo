package com.tekfilo.jewellery.goodsinward.repository;

import com.tekfilo.jewellery.goodsinward.entity.GoodsInwardChargesEntity;
import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GoodsInwardChargesRepository extends JpaRepository<GoodsInwardChargesEntity, Integer> {

    @Query("select c from GoodsInwardChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<GoodsInwardChargesEntity> findAllCharges(Integer invId);

    @Query("FROM GoodsInwardChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<GoodsInwardChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update GoodsInwardChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

}
