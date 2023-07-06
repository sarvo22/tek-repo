package com.tekfilo.sso.role.repository;

import com.tekfilo.sso.role.entity.RolePrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilegeEntity, Integer>, JpaSpecificationExecutor {

    @Query("from RolePrivilegeEntity where isDeleted = 0 and roleId = :id")
    List<RolePrivilegeEntity> finAllByRoleId(Integer id);

    @Query("from RolePrivilegeEntity where isDeleted = 0 and roleId in (:ids)")
    List<RolePrivilegeEntity> findAllUserRolesByUserId(List<Integer> ids);
}
