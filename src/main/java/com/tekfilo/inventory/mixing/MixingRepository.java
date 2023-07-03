package com.tekfilo.inventory.mixing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface MixingRepository extends JpaRepository<MixingEntity, Integer>, JpaSpecificationExecutor {
    @Query("select b from MixingEntity b where b.isDeleted = 0")
    Page<MixingEntity> findAllMixing(Pageable pageable);


    @Modifying
    @Transactional
    @Query("update MixingEntity set isDeleted = 1, modifiedBy=:modifiedBy, modifiedDt=:modifiedDt where isDeleted = 0 and id in (:ids)")
    void deleteMainByIdList(List<Integer> ids, Integer modifiedBy, Timestamp modifiedDt) throws SQLException;
}
