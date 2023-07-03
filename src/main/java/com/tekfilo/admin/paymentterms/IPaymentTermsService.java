package com.tekfilo.admin.paymentterms;

import com.tekfilo.admin.base.FilterClause;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPaymentTermsService {

    Page<PaymentTermsEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection,
                                     List<FilterClause> filterClause);

    PaymentTermsEntity save(PaymentTermsDto paymentTermsDto) throws Exception;

    void modify(PaymentTermsDto paymentTermsDto) throws Exception;

    PaymentTermsEntity findById(Integer id);

    void remove(PaymentTermsEntity entity) throws Exception;


    List<PaymentTermsEntity> findAllEntitiesByIds(List<Integer> ids);

    void removeAll(List<PaymentTermsEntity> entities) throws Exception;

    void lock(List<PaymentTermsEntity> entities) throws Exception;

    void unlock(List<PaymentTermsEntity> entities) throws Exception;
}
