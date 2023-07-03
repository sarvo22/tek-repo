package com.tekfilo.sso.parameter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<ParameterEntity, Integer>, JpaSpecificationExecutor {

    @Query("select p from ParameterEntity p where p.isDeleted = 0 and p.parameterGroup = :parameterGroup")
    List<ParameterEntity> getParameterList(String parameterGroup);

}
