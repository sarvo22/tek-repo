package com.tekfilo.jewellery.production.studinvoice.repository;

import com.tekfilo.jewellery.production.factoryinvoice.entity.FactoryInvoiceMainEntity;
import com.tekfilo.jewellery.production.studinvoice.entity.StudInvoiceMainEntity;
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
public interface StudInvoiceMainRepository extends JpaRepository<StudInvoiceMainEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM StudInvoiceMainEntity where isDeleted = 0 and id in (:mainIds)")
    List<StudInvoiceMainEntity> findAllMainByIds(List<Integer> mainIds);

    @Modifying
    @Transactional
    @Query("update StudInvoiceMainEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainById(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
