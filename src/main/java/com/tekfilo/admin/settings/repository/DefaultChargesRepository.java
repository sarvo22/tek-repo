package com.tekfilo.admin.settings.repository;

import com.tekfilo.admin.settings.entity.DefaultChargesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefaultChargesRepository extends JpaRepository<DefaultChargesEntity, Integer>, JpaSpecificationExecutor {
    @Query("FROM DefaultChargesEntity  where isLocked = 0 and isDeleted = 0 and invType = :invType")
    List<DefaultChargesEntity> findAllByInvoiceType(String invType);
}
