package com.tekfilo.jewellery.production.factoryinvoice.repository;

import com.tekfilo.jewellery.production.factoryinvoice.entity.FactoryInvoiceDetailEntity;
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
public interface FactoryInvoiceDetailRepository extends JpaRepository<FactoryInvoiceDetailEntity, Integer>, JpaSpecificationExecutor {

    @Query("select a from FactoryInvoiceDetailEntity a where a.isDeleted = 0 and a.invoiceId = :id")
    List<FactoryInvoiceDetailEntity> findAllDetail(Integer id);

    @Query("select a from FactoryInvoiceDetailEntity a where a.isDeleted = 0 and a.invoiceId in (:mainIds)")
    List<FactoryInvoiceDetailEntity> findAllDetailMainByIds(List<Integer> mainIds);


    @Modifying
    @Transactional
    @Query("update FactoryInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteDetailById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update FactoryInvoiceDetailEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and invoiceId = :id")
    void deleteDetailByMainId(Integer id, Integer modifiedBy, Timestamp modifiedDt);
}
