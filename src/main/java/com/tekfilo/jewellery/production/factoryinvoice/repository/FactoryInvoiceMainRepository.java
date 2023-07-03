package com.tekfilo.jewellery.production.factoryinvoice.repository;

import com.tekfilo.jewellery.production.factoryinvoice.entity.FactoryInvoiceMainEntity;
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
public interface FactoryInvoiceMainRepository extends JpaRepository<FactoryInvoiceMainEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM FactoryInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<FactoryInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update FactoryInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Modifying
    @Transactional
    @Query("update FactoryInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id = :id")
    void deleteByMainId(Integer id, Integer modifiedBy, Timestamp modifiedDt);
}
