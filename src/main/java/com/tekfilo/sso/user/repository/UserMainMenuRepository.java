package com.tekfilo.sso.user.repository;

import com.tekfilo.sso.user.entity.UserMenus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMainMenuRepository extends JpaRepository<UserMenus, Integer> {

}
