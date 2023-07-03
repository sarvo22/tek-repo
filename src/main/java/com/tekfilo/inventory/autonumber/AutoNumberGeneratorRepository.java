package com.tekfilo.inventory.autonumber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoNumberGeneratorRepository extends JpaRepository<AutoNumberGeneratorEntity, Integer> {


    @Query(value = "SELECT * FROM tbl_auto_number_generator where company_id=:currentCompany and voucher_type = :voucherType LIMIT 1 FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<AutoNumberGeneratorEntity> getNextNumber(String voucherType, Integer currentCompany);

    @Query(value = "SELECT * FROM tbl_auto_number_generator where company_id=:currentCompany and voucher_type = :voucherType LIMIT 1 ", nativeQuery = true)
    List<AutoNumberGeneratorEntity> getNextDisplayNumber(String voucherType, Integer currentCompany);
}
