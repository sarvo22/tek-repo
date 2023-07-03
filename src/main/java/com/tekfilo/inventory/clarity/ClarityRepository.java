package com.tekfilo.inventory.clarity;

import com.tekfilo.inventory.color.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClarityRepository extends JpaRepository<ClarityEntity, Integer>, JpaSpecificationExecutor {
    @Query("select cu from ClarityEntity cu where cu.isDeleted = 0 and cu.companyId = :currentCompany and cu.group.groupType = :category")
    List<ClarityEntity> findAllClarity(String category, Integer currentCompany);

    @Query("select count(1) from ClarityEntity where lower(clarityName) = :clarityName")
    int checkNameExists(String clarityName);

    @Query("from ClarityEntity where lower(clarityName) = :name")
    List<ClarityEntity> findAllByName(String name);
}
