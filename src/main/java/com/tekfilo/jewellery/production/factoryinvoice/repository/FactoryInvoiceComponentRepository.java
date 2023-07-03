package com.tekfilo.jewellery.production.factoryinvoice.repository;

import com.tekfilo.jewellery.production.factoryinvoice.entity.FactoryInvoiceComponentEntity;
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
public interface FactoryInvoiceComponentRepository extends JpaRepository<FactoryInvoiceComponentEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM FactoryInvoiceComponentEntity c where c.isDeleted = 0 and c.invoiceDetailId = :invoiceDetailId")
    List<FactoryInvoiceComponentEntity> findComponentsByDetailId(Integer invoiceDetailId);

    @Query("FROM FactoryInvoiceComponentEntity c where c.isDeleted = 0 and c.invoiceId in (:mainIds)")
    List<FactoryInvoiceComponentEntity> findAllComponentMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update FactoryInvoiceComponentEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteComponentById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update FactoryInvoiceComponentEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and invoiceId = :id ")
    void deleteComponentByMainId(Integer id, Integer modifiedBy, Timestamp modifiedDt);
}
