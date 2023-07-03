package com.tekfilo.account.transaction.partyregister;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRegisterMainRepository extends JpaRepository<PartyRegisterMainEntity, String>, JpaSpecificationExecutor {

    @Query("FROM PartyRegisterMainEntity p where isDeleted = 0 and partyId = :customerId and partyType =  'CUSTOMER' and currency = :currency and ((invoiceAmount - settlementAmount) <> 0)")
    List<PartyRegisterMainEntity> findallCustomerPendingList(Integer customerId, String currency);

    @Query("FROM PartyRegisterMainEntity p where isDeleted = 0 and partyId = :customerId and partyType =  'CUSTOMER' and ((invoiceAmount - settlementAmount) <> 0)")
    List<PartyRegisterMainEntity> findallCustomerPendingList(Integer customerId);

    @Query("FROM PartyRegisterMainEntity p where isDeleted = 0 and partyId = :supplierId and partyType =  'SUPPLIER' and currency = :currency and ((invoiceAmount - settlementAmount) <> 0)")
    List<PartyRegisterMainEntity> findallSupplierPendingList(Integer supplierId, String currency);

    @Query("FROM PartyRegisterMainEntity p where isDeleted = 0 and partyId = :supplierId and partyType =  'SUPPLIER' and ((invoiceAmount - settlementAmount) <> 0)")
    List<PartyRegisterMainEntity> findallSupplierPendingList(Integer supplierId);

}
