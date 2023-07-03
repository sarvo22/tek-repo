package com.tekfilo.admin.paymentterms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTermsRepository extends JpaRepository<PaymentTermsEntity, Integer>, JpaSpecificationExecutor {
}
