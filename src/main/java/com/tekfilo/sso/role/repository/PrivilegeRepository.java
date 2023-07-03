package com.tekfilo.sso.role.repository;

import com.tekfilo.sso.role.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

    @Query("select p from Privilege p where p.isDeleted = 0")
    List<Privilege> findAllPrivileges();
}
