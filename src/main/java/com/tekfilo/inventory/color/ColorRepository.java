package com.tekfilo.inventory.color;

import com.tekfilo.inventory.cut.CutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Integer>, JpaSpecificationExecutor {

    @Query("select cu from ColorEntity cu where cu.isDeleted = 0 and cu.companyId = :currentCompany and cu.group.groupType = :category")
    List<ColorEntity> findAllColors(String category, Integer currentCompany);

    @Query("select count(1) from ColorEntity where lower(colorName) = :colorName")
    int checkNameExists(String colorName);

    @Query("from ColorEntity where lower(colorName) = :name")
    List<ColorEntity> findAllByName(String name);
}
