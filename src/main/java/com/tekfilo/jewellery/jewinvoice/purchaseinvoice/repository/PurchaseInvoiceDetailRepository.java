package com.tekfilo.jewellery.jewinvoice.purchaseinvoice.repository;

import com.tekfilo.jewellery.jewinvoice.purchaseinvoice.entity.PurchaseInvoiceDetailEntity;
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
public interface PurchaseInvoiceDetailRepository extends JpaRepository<PurchaseInvoiceDetailEntity, Integer>, JpaSpecificationExecutor {
    @Query("select a from PurchaseInvoiceDetailEntity a where a.isDeleted = 0 and a.invId = :id")
    List<PurchaseInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("select a from PurchaseInvoiceDetailEntity a where a.isDeleted = 0 and a.productId = :productId")
    List<PurchaseInvoiceDetailEntity> findAllDetailByProductId(Integer productId);

    @Modifying
    @Transactional
    @Query("update PurchaseInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM PurchaseInvoiceDetailEntity where isDeleted = 0 and invId in (:mainIds)")
    List<PurchaseInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);

    @Query("from PurchaseInvoiceDetailEntity a where a.isDeleted = 0 and  referenceInvoiceId = :referenceInvoiceId and referenceInvoiceType = :referenceInvoiceType")
    List<PurchaseInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
