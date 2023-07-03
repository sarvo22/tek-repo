package com.tekfilo.admin.vouchertype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoNumberGeneratorRepository extends JpaRepository<AutoNumberGeneratorEntity, Integer> {

    @Query("from AutoNumberGeneratorEntity e where e.isDeleted = 0 and e.voucherType = :voucherType and e.companyId = :companyId")
    List<AutoNumberGeneratorEntity> findByVoucherType(String voucherType, Integer companyId);
}
