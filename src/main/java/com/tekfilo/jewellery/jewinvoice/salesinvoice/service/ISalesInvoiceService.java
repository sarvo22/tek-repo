package com.tekfilo.jewellery.jewinvoice.salesinvoice.service;

import com.tekfilo.jewellery.base.FilterClause;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceChargesDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceDetailDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceMainDto;
import com.tekfilo.jewellery.jewinvoice.common.InvoiceRequestPayload;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceChargesEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceDetailEntity;
import com.tekfilo.jewellery.jewinvoice.salesinvoice.entity.SalesInvoiceMainEntity;
import com.tekfilo.jewellery.product.entity.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISalesInvoiceService {

    Page<SalesInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    SalesInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception;

    void modify(InvoiceMainDto invoiceMainDto) throws Exception;

    SalesInvoiceMainEntity findById(Integer id);

    void remove(SalesInvoiceMainEntity entity);


    List<SalesInvoiceDetailEntity> findAllDetail(Integer invId);

    SalesInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    SalesInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(SalesInvoiceDetailEntity entity);


    List<SalesInvoiceChargesEntity> findAllICharges(Integer invId);

    SalesInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    SalesInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(SalesInvoiceChargesEntity entity);

    List<SalesInvoiceMainEntity> findMain();

    SalesInvoiceMainEntity createSalesInvoice(InvoiceRequestPayload salesInvoicePayload) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    List<SalesInvoiceDetailEntity> findDetailByProductId(Integer productId);

    void changeStatus(Integer id) throws Exception;

    List<SalesInvoiceDetailEntity> findDetailByIdWithStock(Integer salesInvoiceId);

    List<SalesInvoiceDetailEntity> findPendingReturnList(Integer partyId, String currency);

    List<SalesInvoiceDetailEntity> findPendingReturnListByProduct(Integer partyId, String currency, Integer productId);

    void removeAll(SalesInvoiceMainEntity entity, List<SalesInvoiceDetailEntity> detailEntities, List<SalesInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<SalesInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<SalesInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<SalesInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<SalesInvoiceMainEntity> entities) throws Exception;

    void unlock(List<SalesInvoiceMainEntity> entities) throws Exception;

    List<SalesInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
