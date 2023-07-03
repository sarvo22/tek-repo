package com.tekfilo.jewellery.goodsoutward.repository;

import com.tekfilo.jewellery.goodsoutward.entity.GoodsOutwardDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GoodsOutwardDetailRepository extends JpaRepository<GoodsOutwardDetailEntity, Integer> {
    @Query("select a from GoodsOutwardDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<GoodsOutwardDetailEntity> findAllDetail(Integer id);


    @Query("FROM GoodsOutwardDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<GoodsOutwardDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update GoodsOutwardDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

}
