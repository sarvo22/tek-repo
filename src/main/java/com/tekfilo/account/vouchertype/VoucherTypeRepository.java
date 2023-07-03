package com.tekfilo.account.vouchertype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherTypeRepository extends JpaRepository<VoucherTypeEntity, String> {

    @Query("FROM VoucherTypeEntity v where v.isDeleted = 0 and v.voucherType = :voucherType and companyId = :companyId")
    List<VoucherTypeEntity> findVoucherType(String voucherType, Integer companyId);
}
