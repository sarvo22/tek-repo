package com.tekfilo.jewellery.design.repository;

import com.tekfilo.jewellery.design.entity.DesignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface DesignRepository extends JpaRepository<DesignEntity, Integer>, JpaSpecificationExecutor {

    @Modifying
    @Transactional
    @Query("update DesignEntity m set m.imageUrl = :documentUrls where m.id = :id")
    void updateDocumentUrl(Integer id, String documentUrls);


    @Modifying
    @Transactional
    @Query("update DesignEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDate=:modifiedDt where isDeleted = 0 and id in (:designIds)")
    void deleteAllByIds(List<Integer> designIds, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;

    @Query("FROM DesignEntity as e WHERE e.designNo IN (:names) and companyId = :companyId")
    List<DesignEntity> findByDesignNos(@Param("names") List<String> names, Integer companyId);
}
