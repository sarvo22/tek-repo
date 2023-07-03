package com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.repository;

import com.tekfilo.jewellery.jewinvoice.memopurchaseinvoice.entity.MemoPurchaseInvoiceDetailEntity;
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
public interface MemoPurchaseInvoiceDetailRepository extends JpaRepository<MemoPurchaseInvoiceDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from MemoPurchaseInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<MemoPurchaseInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("select a from MemoPurchaseInvoiceDetailEntity a where a.isDeleted = 0 and a.invId in (:ids)")
    List<MemoPurchaseInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("update MemoPurchaseInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
