package com.tekfilo.jewellery.master.repository;

import com.tekfilo.jewellery.master.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Integer>, JpaSpecificationExecutor {
}
