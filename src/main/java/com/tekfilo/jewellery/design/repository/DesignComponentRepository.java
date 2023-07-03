package com.tekfilo.jewellery.design.repository;

import com.tekfilo.jewellery.design.entity.DesignComponentEntity;
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
public interface DesignComponentRepository extends JpaRepository<DesignComponentEntity, Integer>, JpaSpecificationExecutor {

    @Query("FROM DesignComponentEntity d where d.isDeleted = 0 and d.designId = :designId")
    List<DesignComponentEntity> findAllByMainId(Integer designId);

    @Modifying
    @Transactional
    @Query("update DesignComponentEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDate=:modifiedDt where isDeleted = 0 and designId in (:designIds)")
    void deleteAllByDesignIds(List<Integer> designIds, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
