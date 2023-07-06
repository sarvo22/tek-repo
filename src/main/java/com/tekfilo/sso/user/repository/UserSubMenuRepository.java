package com.tekfilo.sso.user.repository;

import com.tekfilo.sso.user.entity.UserSubMenus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubMenuRepository extends JpaRepository<UserSubMenus, Integer> {
}
