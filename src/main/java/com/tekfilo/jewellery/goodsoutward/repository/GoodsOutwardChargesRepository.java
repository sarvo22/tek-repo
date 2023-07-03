package com.tekfilo.jewellery.goodsoutward.repository;

import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GoodsOutwardChargesRepository extends JpaRepository<GoodsOutwardChargesEntity, Integer> {

    @Query("select c from GoodsOutwardChargesEntity c where c.isDeleted = 0 and c.invId = :invId")
    List<GoodsOutwardChargesEntity> findAllCharges(Integer invId);


    @Query("FROM GoodsOutwardChargesEntity where isDeleted = 0 and invId in (:mainIds)")
    List<GoodsOutwardChargesEntity> findAllChargesMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update GoodsOutwardChargesEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteChargesById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

}
