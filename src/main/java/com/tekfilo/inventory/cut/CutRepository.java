package com.tekfilo.inventory.cut;

import com.tekfilo.inventory.shape.ShapeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CutRepository extends JpaRepository<CutEntity, Integer>, JpaSpecificationExecutor {

    @Query("select cu from CutEntity cu where cu.isDeleted = 0 and cu.companyId = :currentCompany and cu.group.groupType = :category")
    List<CutEntity> findAllCuts(String category, Integer currentCompany);

    @Query("select count(1) from CutEntity where lower(cutName) = :cutName")
    int checkNameExists(String cutName);

    @Query("from CutEntity where lower(cutName) = :name")
    List<CutEntity> findAllByName(String name);
}
