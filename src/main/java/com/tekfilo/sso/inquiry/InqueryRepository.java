package com.tekfilo.sso.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InqueryRepository extends JpaRepository<InqueryEntity, Integer> {
}
