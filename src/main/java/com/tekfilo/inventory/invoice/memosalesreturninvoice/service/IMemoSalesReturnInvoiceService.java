package com.tekfilo.inventory.invoice.memosalesreturninvoice.service;

import com.tekfilo.inventory.base.FilterClause;
import com.tekfilo.inventory.invoice.common.InvoiceChargesDto;
import com.tekfilo.inventory.invoice.common.InvoiceDetailDto;
import com.tekfilo.inventory.invoice.common.InvoiceMainDto;
import com.tekfilo.inventory.invoice.common.InvoiceRequestPayload;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceChargesEntity;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceDetailEntity;
import com.tekfilo.inventory.invoice.memosalesreturninvoice.entity.MemoSalesReturnInvoiceMainEntity;
import com.tekfilo.inventory.product.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IMemoSalesReturnInvoiceService {

    Page<MemoSalesReturnInvoiceMainEntity> findAll(int pageNo, int pageSize, String sortName, String sortDirection, List<FilterClause> filterClauses);

    MemoSalesReturnInvoiceMainEntity save(InvoiceMainDto invoiceMainDto) throws Exception;

    void modify(InvoiceMainDto invoiceMainDto) throws Exception;

    MemoSalesReturnInvoiceMainEntity findById(Integer id);

    void remove(MemoSalesReturnInvoiceMainEntity entity);


    List<MemoSalesReturnInvoiceDetailEntity> findAllDetail(Integer invId);

    MemoSalesReturnInvoiceDetailEntity saveDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    void modifyDetail(InvoiceDetailDto invoiceDetailDto) throws Exception;

    MemoSalesReturnInvoiceDetailEntity findDetailById(Integer id);

    void removeDetail(MemoSalesReturnInvoiceDetailEntity entity);


    List<MemoSalesReturnInvoiceChargesEntity> findAllICharges(Integer invId);

    MemoSalesReturnInvoiceChargesEntity saveCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    void modifyCharges(InvoiceChargesDto invoiceChargesDto) throws Exception;

    MemoSalesReturnInvoiceChargesEntity findChargesById(Integer id);

    void removeCharges(MemoSalesReturnInvoiceChargesEntity entity);

    List<MemoSalesReturnInvoiceMainEntity> findMain();

    MemoSalesReturnInvoiceMainEntity createSalesInvoice(InvoiceRequestPayload invoiceRequestPayload) throws Exception;

    List<ProductEntity> getProductList(String searchKey);

    String confirmSales(Integer memoInvoiceId) throws Exception;

    void returnMemoSales(Integer memoInvoiceId) throws Exception;

    String confirmPartialSales(Integer memoInvoiceMainId, List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception;

    void partialReturnMemoSales(Integer memoInvoiceId, List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception;

    void saveAllDetail(List<InvoiceDetailDto> invoiceDetailDtoList) throws Exception;


    void changeStatus(Integer id) throws Exception;

    void removeAll(MemoSalesReturnInvoiceMainEntity entity, List<MemoSalesReturnInvoiceDetailEntity> detailEntities, List<MemoSalesReturnInvoiceChargesEntity> chargesEntities) throws Exception;

    void removeAll(List<Integer> mainIds, List<Integer> detailIds, List<Integer> chargesIds) throws Exception;

    List<MemoSalesReturnInvoiceMainEntity> findAllEntitiesByIds(List<Integer> mainIds);

    List<MemoSalesReturnInvoiceDetailEntity> findAllDetailByMainIds(List<Integer> mainIds);

    List<MemoSalesReturnInvoiceChargesEntity> findAllChargesByMainIds(List<Integer> mainIds);

    void lock(List<MemoSalesReturnInvoiceMainEntity> entities) throws Exception;

    void unlock(List<MemoSalesReturnInvoiceMainEntity> entities) throws Exception;

    List<MemoSalesReturnInvoiceDetailEntity> findAllDetailByReferenceInvoiceIdAndType(Integer referenceInvoiceId, String referenceInvoiceType);
}
