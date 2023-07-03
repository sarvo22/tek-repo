package com.tekfilo.sso.role.repository;

import com.tekfilo.sso.role.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    @Query("FROM RoleEntity L WHERE L.roleName = :roleName")
    List<RoleEntity> findByName(@Param("roleName") String roleName);


    @Query("from RoleEntity r where r.isDeleted = 0")
    Page<RoleEntity> findAll(Pageable pageable);

    @Query("select r from RoleEntity r where r.isDeleted = 0 and r.companyId = :companyId")
    List<RoleEntity> findAllRoles(Integer companyId);
}
