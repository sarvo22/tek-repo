package com.tekfilo.inventory.mixing;


import com.tekfilo.inventory.invoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MixingDetailRepository extends JpaRepository<MixingDetailEntity, Integer> {
    @Query("select de from MixingDetailEntity de where de.isDeleted = 0 and  mixingId = :id")
    List<MixingDetailEntity> findAllDetailByMainId(Integer id);

    @Query("select a from MixingDetailEntity a where a.isDeleted = 0 and a.mixingId in (:ids)")
    List<MixingDetailEntity> findAllDetailByMainIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("update MixingDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

}
