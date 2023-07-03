package com.tekfilo.sso.apiconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<ApiEntity, Integer> {
}
