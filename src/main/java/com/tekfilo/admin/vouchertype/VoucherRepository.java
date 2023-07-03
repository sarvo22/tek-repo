package com.tekfilo.admin.vouchertype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherTypeEntity, String>, JpaSpecificationExecutor {


    @Query("select count(1) from VoucherTypeEntity where voucherType = :voucherType and companyId = :currentCompany")
    int checkVoucherTypeExist(String voucherType, Integer currentCompany);
}
