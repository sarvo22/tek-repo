package com.tekfilo.inventory.invoice.salesreturninvoice.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.salesreturninvoice.entity.SalesReturnInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.salesreturninvoice.entity.SalesReturnInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.salesreturninvoice.entity.SalesReturnInvoiceMainEntity;
import com.tekfilo.inventory.product.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISalesReturnInvoiceService {

    Page<SalesReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClause);

    SalesReturnInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception;

    void modify(InvoiceMainDto invoiceMainDto) throws Exception;

    SalesReturnInvoiceMainEntity findById(Integer id);

    void remove(SalesReturnInvoiceMainEntity entity);


    List<SalesReturnInvoiceDetailEntity> findAllDetail(Integer invId);

    SalesReturnInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    SalesReturnInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(SalesReturnInvoiceDetailEntity entity);


    List<SalesReturnInvoiceChargesEntity> findAllICharges(Integer invId);

    SalesReturnInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    SalesReturnInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(SalesReturnInvoiceChargesEntity entity);

    List<SalesReturnInvoiceMainEntity> findMain();

    SalesReturnInvoiceMainEntity createSalesInvoice(InvoiceRequestPayload salesInvoicePayload) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    void saveAllDetail(List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception;

    void changeStatus(Integer id) throws Exception;

    List<SalesReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);

    void removeAll(SalesReturnInvoiceMainEntity entity, List<SalesReturnInvoiceDetailEntity> detailEntities, List<SalesReturnInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<SalesReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<SalesReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<SalesReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<SalesReturnInvoiceMainEntity> entities) throws Exception;

    void unlock(List<SalesReturnInvoiceMainEntity> entities) throws Exception;
}
