package com.tekfilo.inventory.settlement.paymentreceived.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.settlement.paymentreceived.PaymentReceivedRequestPayload;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PaymentMainDto;
import com.tekfilo.inventory.settlement.paymentreceived.dto.PendingInvoiceDto;
import com.tekfilo.inventory.settlement.paymentreceived.entity.PaymentReceivedDetailEntity;
import com.tekfilo.inventory.settlement.paymentreceived.entity.PaymentReceivedMainEntity;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.List;

public interface IPaymentReceivedService {
    Page<PaymentReceivedMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    Integer saveAll(PaymentReceivedRequestPayload paymentReceivedRequestPayload) throws Exception;

    PaymentReceivedMainEntity findById(Integer id);

    List<PaymentReceivedDetailEntity> findDetailByMainId(Integer id);

    PaymentReceivedDetailEntity findDetailById(Integer id);

    void deleteDetail(PaymentReceivedDetailEntity entity) throws Exception;

    void deleteMain(Integer id) throws Exception;

    Integer saveMain(PaymentMainDto paymentMainDto) throws Exception;

    void saveDetailList(List<PendingInvoiceDto> detailList) throws Exception;

    List<PaymentReceivedDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    List<PaymentReceivedMainEntity> findAllMainByMainIds(List<Integer> ids);

    List<PaymentReceivedDetailEntity> findAllDetailByMainIds(List<Integer> ids);

    void removeAll(List<PaymentReceivedMainEntity> entityList, List<PaymentReceivedDetailEntity> detailEntities) throws Exception;

    void lock(List<PaymentReceivedMainEntity> entities) throws SQLException;

    void unlock(List<PaymentReceivedMainEntity> entities) throws SQLException;
}
