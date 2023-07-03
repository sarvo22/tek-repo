package com.tekfilo.inventory.settlement.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRegisterMainRepository extends JpaRepository<PartyRegisterMainEntity, String>, JpaSpecificationExecutor {


    @Query("FROM PartyRegisterMainEntity p where isDeleted = 0 and partyId = :customerId and partyType =  'CUSTOMER' and currency = :currency")
    List<PartyRegisterMainEntity> findallCustomerPendingList(Integer customerId, String currency);
}
