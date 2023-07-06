package com.tekfilo.sso.role.repository;

import com.tekfilo.sso.role.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {

    @Query("select u from UserRoleEntity u where u.isDeleted = 0 and u.userId = :userId")
    List<UserRoleEntity> findUserRolesByUserId(Integer userId);

    @Query("select u from UserRoleEntity u where u.isDeleted = 0 and u.userId = :userId and roleId = :roleId")
    List<UserRoleEntity> findUserRolesByRoleAndUserId(Integer roleId, Integer userId);

    @Query("select u from UserRoleEntity u where u.isDeleted = 0 and u.userId = :userId and companyId = :companyId")
    List<UserRoleEntity> findUserRolesByUserIdAndCompanyId(Integer userId, Integer companyId);
}
