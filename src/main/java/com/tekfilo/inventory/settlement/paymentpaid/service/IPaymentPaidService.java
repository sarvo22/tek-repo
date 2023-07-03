package com.tekfilo.inventory.settlement.paymentpaid.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.settlement.paymentpaid.PaymentPaidRequestPayload;
import com.tekfilo.inventory.settlement.paymentpaid.dto.PaymentPaidMainDto;
import com.tekfilo.inventory.settlement.paymentpaid.entity.PaymentPaidDetailEntity;
import com.tekfilo.inventory.settlement.paymentpaid.entity.PaymentPaidMainEntity;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PendingInvoiceDto;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.List;

public interface IPaymentPaidService {
    Page<PaymentPaidMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    Integer saveAll(PaymentPaidRequestPayload paymentPaidRequestPayload) throws Exception;

    PaymentPaidMainEntity findById(Integer id);

    List<PaymentPaidDetailEntity> findDetailByMainId(Integer id);

    PaymentPaidDetailEntity findDetailById(Integer id);

    void deleteDetail(PaymentPaidDetailEntity entity) throws Exception;

    void deleteMain(Integer id) throws Exception;

    Integer saveMain(PaymentPaidMainDto paymentPaidMainDto) throws Exception;

    void saveDetailList(List<PendingInvoiceDto> detailList) throws Exception;

    List<PaymentPaidDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    List<PaymentPaidDetailEntity> findAllDetailBySupplierId(Integer supplierId);

    List<PaymentPaidMainEntity> findAllMainByMainIds(List<Integer> ids);

    List<PaymentPaidDetailEntity> findAllDetailByMainIds(List<Integer> ids);

    void removeAll(List<PaymentPaidMainEntity> entityList, List<PaymentPaidDetailEntity> detailEntities) throws Exception;

    void lock(List<PaymentPaidMainEntity> entities) throws SQLException;

    void unlock(List<PaymentPaidMainEntity> entities) throws SQLException;
}
