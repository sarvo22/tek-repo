package com.tekfilo.account.master.repository;

import com.tekfilo.account.master.PartyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity,String> {
}
