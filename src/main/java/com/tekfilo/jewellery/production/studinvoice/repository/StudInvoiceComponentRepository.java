package com.tekfilo.jewellery.production.studinvoice.repository;

import com.tekfilo.jewellery.production.factoryinvoice.entity.FactoryInvoiceComponentEntity;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceComponentEntity;
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
public interface StudInvoiceComponentRepository extends JpaRepository<StudInvoiceComponentEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM StudInvoiceComponentEntity c where c.isDeleted = 0 and c.invoiceDetailId = :invoiceDetailId")
    List<StudInvoiceComponentEntity> findComponentsByDetailId(Integer invoiceDetailId);


    @Query("FROM StudInvoiceComponentEntity c where c.isDeleted = 0 and c.invoiceId in (:mainIds)")
    List<StudInvoiceComponentEntity> findAllComponentMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update StudInvoiceComponentEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteComponentById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
